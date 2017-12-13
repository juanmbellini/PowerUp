'use strict';
define(['powerUp'], function(powerUp) {

    powerUp.service('PaginationService', ['$log', 'Restangular', function ($log, Restangular) {

        /* ********************************************
         *              Public functions
         * *******************************************/

        /**
         * Adds a "pagination" property to a given Restangularized object to track pagination.
         *
         * @param object            The object to initialize. Must be Restangularized.
         * @param subElement        (Optional) The object's paginated sub-element to fetch later on, if any. For example,
         *                          to track threads call `initialize(Restangular.all('threads'), undefined, ...)`. To
         *                          track a thread's comments, call `initialize(thread, 'comments', ...)`.
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
         * Sets request parameters to be sent on each request under the object's "pagination" property. NOTE: If
         * changing request params when the paginator has already fetched results, consider re-initializing paginator or
         * setting page back to 1. Results may be inconsistent otherwise.
         *
         * @param object        The object to set parameters for. Must be initialized.
         * @param params        The parameters to set.
         */
        function setRequestParams(object, params) {
            if (!isInitialized(object) || params === null || typeof params !== 'object') {
                return;
            }
            object.pagination.params = params;
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
            get(object, successCallback, errorCallback);
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
            var newPageNumber = object.pagination.pageNumber + 1;
            // Avoid going out of range if we have the last page number
            if (typeof object.pagination.lastPageNumber === 'number') {
                newPageNumber = Math.max(object.pagination.lastPageNumber, object.pagination.pageNumber + 1);
            }

            getPage(object, newPageNumber, successCallback, errorCallback);
        }

      /**
       * Gets the next page of data for the given object.
       *
       * @param object            The object to get the next page of. Must be initialized.
       * @param successCallback   Callback called with response on every returned page
       * @param errorCallback     Callback called on any page that causes error. Will stop requests of all further pages.
       */
      function getAllPages(object, successCallback, errorCallback) {
        if (!isInitialized(object)) {
          return;
        }
        if (object.pagination.totalPages && object.pagination.pageNumber && object.pagination.pageNumber > object.pagination.totalPages) {
          return;
        }
        // Not done yet, get current page and enqueue next one
        get(object, function(response) {
          successCallback(response);
          if (object.pagination.pageNumber < object.pagination.totalPages) {
            object.pagination.pageNumber++;
            // Get next page!
            getAllPages(object, successCallback, errorCallback);
          }
        }, errorCallback);
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
            getPage(object, Math.max(1, object.pagination.pageNumber - 1), successCallback, errorCallback);
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
         * @return {boolean|undefined}  Returns undefined on invalid params. Otherwise returns a boolean.
         */
        function hasMorePages(object) {
            if (!isInitialized(object)) {
                return undefined;
            }
            if (!object.pagination.pageNumber) {
                return undefined;
            } else {
                return object.pagination.pageNumber < object.pagination.totalPages;
            }
        }

        /**
         * Checks whether a given object has been initialized with this service. An object is considered initialized
         * when it has all the properties that  the {@link initialize} function sets, regardless of their values.
         *
         * @param object                The object to check.
         * @return {boolean}            Whether the object is initialized.
         */
        function isInitialized(object) {
            return object !== null && typeof object === 'object'
                && object.hasOwnProperty('restangularized') && object.restangularized
                && object.hasOwnProperty('pagination')
                && object.pagination.hasOwnProperty('subElement')
                && object.pagination.hasOwnProperty('pageNumber')
                && object.pagination.hasOwnProperty('pageSize')
                && object.pagination.hasOwnProperty('orderBy')
                && object.pagination.hasOwnProperty('sortDirection');
        }

        /**
         * Helper function, returns an array of page numbers adjacent to the current page for the given initialized
         * object. Ensures that no page will go out of range.
         * NOTE: Pagination information must be set for the passed object.
         *
         * @param object        The initialized object. Must have pagination information set.
         * @param deltaPages    (Optional) How many pages at most to render for each side. E.g. if passed 4 and current
         *                      page is 6, will return [2...10]; if current page is 2 will return [1...10]. Default is 4.
         * @return {Array|null} The page range, or null on invalid parameters.
         */
        function getPageRange(object, deltaPages) {
            if (!isInitialized(object) || !object.pagination.totalPages || parseInt(deltaPages, 10) < 0) {
                return null;
            }
            deltaPages = deltaPages || 4;
            var result = [];
            for (var i = -deltaPages; i <= deltaPages; i++) {
                var page = object.pagination.pageNumber + i;
                if (page >= 1 && page <= object.pagination.totalPages) {
                    result.push(page);
                }
            }
            return result;
        }

        /* ********************************************
         *              Private functions
         * *******************************************/
        function query(object, successCallback, errorCallback) {
            if (!isInitialized(object)) {
                return;
            }
            var param1 = object.pagination.subElement || buildParamsObjectFor(object);  // First or second
            var param2 = object.pagination.subElement && buildParamsObjectFor(object);  // Second or undefined
            object.getList(param1, param2).then(function(response) {
                // Extract extra pagination data from response headers and add data to pagination property
                Object.assign(object.pagination, extractPageMetadata(response));

                if (typeof successCallback !== 'undefined') {
                    successCallback(response);
                }
            }, function(error) {
                if (typeof errorCallback !== 'undefined') {
                    errorCallback(error);
                } else {
                    $log.error('Error getting paginated "' + object.pagination.subElement + '" for ', object, ': ', error);
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
            // Add defined and non-null params to result
            if (object.pagination.hasOwnProperty('params')) {
                for (var key in object.pagination.params) {
                    if (object.pagination.params.hasOwnProperty(key)) {
                        var value = object.pagination.params[key];
                        if (typeof value !== 'undefined' && value !== null) {
                            result[key] = value;
                        }
                    }
                }
            }
            // Add pagination params to result (will overwrite search params in previous conditional)
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
            isInitialized: isInitialized,
            setRequestParams: setRequestParams,
            get: get,
            getPage: getPage,
            getNextPage: getNextPage,
            getAllPages: getAllPages,
            getPreviousPage: getPreviousPage,
            getFirstPage: getFirstPage,
            getLastPage: getLastPage,
            hasMorePages: hasMorePages,
            getPageRange: getPageRange
        };
    }]);
});
