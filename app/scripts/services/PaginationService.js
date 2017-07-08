'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.service('PaginationService', ['$log', 'Restangular', function ($log, Restangular) {

        // Needed to access headers
        // TODO change this in general configuration and use it everywhere
        Restangular.setFullResponse(true);

        /* ********************************************
         *              Public functions
         * *******************************************/

        /**
         * Adds a "pagination" property to a given Restangularized object to track pagination.
         *
         * @param object            The object to initialize. Must be Restangularized.
         * @param subElement        The object's paginated sub-element to fetch later on. For example, to track a
         *                          thread's comments, call `initialize(thread, 'comments', ...)`.
         * @param pageNumber        (Optional) The initial page number.
         * @param pageSize          (Optional) The page size.
         * @param orderBy           (Optional) Criterium to order results by.
         * @param sortDirection     (Optional) Whether results should be ordered by `orderBy` in ascending or
         *                          descending order.
         * @return {object}         The initialized object, or `null` on invalid parameters.
         */
        function initialize(object, subElement, pageNumber, pageSize, orderBy, sortDirection) {
            if (!object || !object.hasOwnProperty('restangularized') || !object.restangularized) {
                return null;
            }
            object.pagination = {
                subElement: subElement,
                pageNumber: pageNumber,
                pageSize: pageSize,
                orderBy: orderBy,
                sortDirection: sortDirection
            };
            return object;
        }

        /**
         * Fetch a page of the specified object's sub-element with the object's current configuration.
         *
         * @param object            The object which has pagination info. Must have been initialized previously.
         * @param successCallback   (Optional) Function to call on success. Receives the Restangularized response.
         * @param errorCallback     (Optional) Function to call on error. Receives the error object.
         */
        function get(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            query(object, successCallback, errorCallback);
        }

        /**
         * Gets an arbitrary page number of the given object, with its current configuration (pageSize, order, etc.)
         * Updates the object's current page number.
         *
         * @param object            The object to get a page of. Must be initialized.
         * @param pageNumber        The page number to get.
         * @param successCallback
         * @param errorCallback
         */
        function getPage(object, pageNumber, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            object.pagination.pageNumber = pageNumber;
            query(object, successCallback, errorCallback);
        }

        /**
         * Gets the next page of data for the given object.
         *
         * @param object            The object to get the next page of. Must be initialized.
         * @param successCallback
         * @param errorCallback
         */
        function getNextPage(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            getPage(object, object.pagination.pageNumber + 1, successCallback, errorCallback);
        }

        /**
         * Gets the previous page of data for the given object.
         *
         * @param object            The object to get the previous page of. Must be initialized.
         * @param successCallback
         * @param errorCallback
         */
        function getPreviousPage(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            getPage(object, object.pagination.pageNumber - 1, successCallback, errorCallback);
        }

        /**
         * Gets the first page of data for the given object.
         *
         * @param object            The object to get the first page of. Must be initialized.
         * @param successCallback
         * @param errorCallback
         */
        function getFirstPage(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            getPage(object, 1, successCallback, errorCallback);
        }

        /**
         * Gets the last page of data for the given object.
         *
         * @param object            The object to get the last page of. Must be initialized.
         * @param successCallback
         * @param errorCallback
         */
        function getLastPage(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            if (!object.pagination.lastPageNumber) {
                // Last page number unknown, fetch object and try again
                get(object, function() {
                    getLastPage(object, successCallback, errorCallback);
                }, errorCallback);
                return;
            }

            getPage(object, object.pagination.lastPageNumber, successCallback, errorCallback);
        }

        /**
         * Checks whether the given object has more pages that can be fetched.
         *
         * @param object                The object to check for pages. Must be initialized.
         * @param fetchIfNecessary      (Optional) Whether to fetch the object from the API for metadata, if the object
         *                              hasn't been fetched yet. Defaults to false.
         * @return {boolean|undefined}  Returns undefined when:
         *
         *                                - Receiving invalid params
         *                                - The object hasn't been fetched and fetchIfNecessary is false.
         *                                - There's an error fetching the object
         *                              Otherwise returns a boolean.
         */
        function hasMorePages(object, fetchIfNecessary) {
            if(!isInitialized(object)) {
                return undefined;
            }
            if(!object.pagination.pageNumber) {
                if(fetchIfNecessary === true) {
                    get(object, hasMorePages(object, false), function(error) {
                        $log.error('Error getting ', object, ': ', error);
                        return undefined;
                    });
                } else {
                    return undefined;
                }
            } else {
                return object.pagination.pageNumber < object.pagination.totalPages;
            }
        }

        /* ********************************************
         *              Private functions
         * *******************************************/
        function isInitialized(object) {
            return typeof object === 'object'
                && object.hasOwnProperty('restangularized') && object.restangularized
                && object.hasOwnProperty('pagination')
                && object.pagination.hasOwnProperty('subElement')
                && object.pagination.hasOwnProperty('pageNumber')
                && object.pagination.hasOwnProperty('pageSize')
                && object.pagination.hasOwnProperty('orderBy')
                && object.pagination.hasOwnProperty('sortDirection');
        }

        function query(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            object.getList(object.pagination.subElement, buildParamsObjectFor(object)).then(function(response) {
                // Extract extra pagination data from response headers and add data to pagination property
                Object.assign(object.pagination, extractPageMetadata(response));

                if (typeof successCallback !== 'undefined') {
                    successCallback(response);
                }
            }, function(error) {
                if (typeof errorCallback !== 'undefined') {
                    errorCallback(error);
                } else {
                    $log.error('Error getting paginated "', object.pagination.subElement, '" for ', object, ': ', error);
                }
            });
        }

        /**
         * Copies all defined and non-null pagination properties from the given object. To be used when querying API to
         * send request parameters.
         *
         * @param object    The object from which to copy properties.
         * @return {object} The copied properties.
         */
        function buildParamsObjectFor(object) {
            var result = {};
            if (!isInitialized(object)) {
                return result;
            }
            ['pageNumber', 'pageSize', 'orderBy', 'sortDirection'].forEach(function(key) {
                if (typeof object.pagination[key] !== 'undefined' && object.pagination[key] !== null) {
                    result[key] = object.pagination[key];
                }
            });
            return result;
        }

        function extractPageMetadata(response) {
            var metadata = response.headers();
            return {
                pageNumber: parseInt(metadata['x-page-number'], 10),
                totalPages: parseInt(metadata['x-total-pages'], 10),
                totalElements: parseInt(metadata['x-overall-amount-of-elements'], 10),
                elementsInPage: parseInt(metadata['x-amount-of-elements'], 10)
            };
        }

        // Public exported functions
        return {
            initialize: initialize,
            get: get,
            getPage: getPage,
            getNextPage: getNextPage,
            getPreviousPage: getPreviousPage,
            getFirstPage: getFirstPage,
            getLastPage: getLastPage,
            hasMorePages: hasMorePages
        };
    }]);
});
