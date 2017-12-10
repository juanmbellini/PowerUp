'use strict';
define(['powerUp', 'loadingCircle', 'loadingCircleSmall', 'ratingStars', 'PaginationService'], function(powerUp) {

    powerUp.controller('SearchCtrl', ['$scope', '$timeout', '$location', '$log', 'Restangular', 'PaginationService', function ($scope, $timeout, $location, $log, Restangular, PaginationService) {
        Restangular.setFullResponse(true);

        $scope.searchParams = {
            name: $location.search().name,
            publisher: $location.search().publisher,
            developer: $location.search().developer,
            genre: $location.search().genre,
            keyword: $location.search().keyword,
            platform: $location.search().platform,
            // Pagination
            orderBy: $location.search().orderBy || 'name',
            sortDirection: $location.search().sortDirection || 'asc',
            pageSize: $location.search().pageSize || 25,
            pageNumber: $location.search().pageNumber || 1
        };

        // Search parameters which will trigger a new search when changed
        var paginationSearchParams = ['orderBy', 'sortDirection', 'pageSize', 'pageNumber'];

        var defaultSortDirections = {
            name: 'asc',
            avg_score: 'desc',  // eslint-disable-line camelcase
            release: 'desc'
        };

        // Sanitize search param values
        for (var key in $scope.searchParams) {
            if ($scope.searchParams.hasOwnProperty(key)) {
                if ($scope.searchParams[key] === true) {
                    delete $scope.searchParams[key];
                }
            }
        }

        // Sanitize numeric search param values
        if (isNaN(parseInt($scope.searchParams.pageSize, 10))) {
            $scope.searchParams.pageSize = 25;
        } else {
            $scope.searchParams.pageSize = parseInt($scope.searchParams.pageSize, 10);
        }
        if (isNaN(parseInt($scope.searchParams.pageNumber, 10))) {
            $scope.searchParams.pageNumber = 1;
        } else {
            $scope.searchParams.pageNumber = parseInt($scope.searchParams.pageNumber, 10);
        }


        $scope.games = null;

        $scope.submitSearch = function (resetPageNumber) {
            if (resetPageNumber === true) {
                $scope.searchParams.pageNumber = 1;
            }
            $log.debug('Reloading Search with specified parameters: ', $scope.searchParams);
            $location.search($scope.searchParams);
            $location.path('search');
        };

        $scope.getPageRange = function(deltaPages) {
            return PaginationService.getPageRange($scope.gamesPaginator, deltaPages);
        };

        $scope.clearFilters = function () {
            $scope.searchParams.developer = [];
            $scope.searchParams.publisher = [];
            $scope.searchParams.genre = [];
            $scope.searchParams.platform = [];
            setUpFilters();
        };

        /**
         * Toggles sorting direction or sets new order-by criterion and default sort direction.  Always resets page
         * number to 1.
         *
         * @param orderBy The new criterion to order by.
         */
        $scope.changeOrderBy = function (orderBy) {
            if ($scope.searchParams.orderBy === orderBy) {
                $scope.toggleSortDirection();
            } else {
                $scope.searchParams.orderBy = orderBy;
                $scope.searchParams.sortDirection = defaultSortDirections[orderBy];
            }
            $scope.searchParams.pageNumber = 1;
        };

        $scope.toggleSortDirection = function () {
            if ($scope.searchParams.sortDirection === 'asc') {
                $scope.searchParams.sortDirection = 'desc';
            } else if ($scope.searchParams.sortDirection === 'desc') {
                $scope.searchParams.sortDirection = 'asc';
            }
            $log.warn('Called toogleSortDirection but sort direction is neither asc nor desc. Doing nothing.');
        };

        $scope.setPageNumber = function (number) {
            if (!$scope.gamesPaginator.pagination.totalPages) {
                return;
            }
            if (number >= 1 && number <= $scope.gamesPaginator.pagination.totalPages) {
                $scope.searchParams.pageNumber = number;
            }
        };

        // Pagination control
        $scope.pageSizes = [25, 50, 100];
        $scope.resetPageNumberOnSubmit = false;
        $scope.gamesPaginator = PaginationService.initialize(Restangular.all('games'), undefined, $scope.searchParams.pageNumber, $scope.searchParams.pageSize, $scope.searchParams.orderBy, $scope.searchParams.sortDirection);
        PaginationService.setRequestParams($scope.gamesPaginator, $scope.searchParams);
        PaginationService.get($scope.gamesPaginator, function (response) {
            $scope.games = response.data;
            $log.debug('Found ' + $scope.games.length + ' games');
        }, function (error) {
            // TODO do something more useful
            $log.error('Error performing search: ', error);
            $scope.games = [];
        });

        $scope.validPageSizes = function() {
            var result = [];
            var pagination = $scope.gamesPaginator.pagination;
            if (!pagination.totalElements) {
                return result;
            }
            $scope.pageSizes.forEach(function(pageSize, index, pageSizes) {
                if (pagination.totalElements >= pageSize || (index > 0 && pagination.totalElements > pageSizes[index - 1])) {
                    result.push(pageSize);
                }
            });
            var customPageSize = $scope.pageSizes.indexOf($scope.searchParams.pageSize) === -1 ? $scope.searchParams.pageSize : null;
            if (customPageSize) {
                result.push(customPageSize);
                result.sort(function(a, b) {
                    return a - b;
                });
            }
            return result;
        };

        // Reload page on pagination changes
        $scope.$watchCollection(function () {
            return extractProperties(paginationSearchParams, $scope.searchParams);
        }, function (newVal, oldVal) {
            if (typeof oldVal === 'undefined' || angular.equals(newVal, oldVal)) {
                return; // Initial change, ignore
            }
            // If page number didn't change, then a different pagination param changed. Reset page number.
            var resetPageNumber = newVal.pageNumber === oldVal.pageNumber;
            $scope.submitSearch(resetPageNumber);
        });

        // Open the filters collapsible when games are found and there are filters active
        $scope.$watchCollection('games', function(newVal, oldVal) {
            if (typeof oldVal === 'undefined' || angular.equals(newVal, oldVal)) {
                return; // Initial change, ignore
            }
            if ($scope.filtersReady && hasFilters()) {
                openFiltersCollapsible();
            }
        });

        // Enable filters when ready
        if ($scope.filtersReady) {
            $log.debug('Filters were already loaded, enabling filters');
            setUpFilters();
        } else {
            $log.debug('Filters not loaded, waiting');
            $scope.$watch('filtersReady', function (newVal, oldVal) {
                if (newVal === true) {
                    $log.debug('Filters ready, enabling filters');
                    setUpFilters();
                }
            });
        }

        /* ********************************************
         *          PRIVATE FUNCTIONS
         * *******************************************/
        // Adapted from https://gist.github.com/Daniel-Hug/aacee325605abaa72998
        function arrayToObj(array, fn) {
            var obj = {};
            var len = array.length;
            for (var i = 0; i < len; i++) {
                var key = array[i].value;
                obj[key] = null;            // Set value to image URL if you want to show a circular image for an entry
            }
            return obj;
        }

        /**
         * Extract a subset of a specified objet's properties.
         *
         * @return {object}     The sub-object.
         */
        function extractProperties(properties, object) {
            var result = {};
            properties.forEach(function (key) {
                if (object.hasOwnProperty(key)) {
                    result[key] = object[key];
                }
            });
            return result;
        }

        /**
         * Builds an array of objects adhering to the format specified by Materialize chips for initial data.
         *
         * @param data              The data to process.
         * @param filterCategory    The filter category this data belongs to.
         * @return {Array}          The processed data, ready to use in Materialize chips initial data.
         */
        function initialChipData(data, filterCategory) {
            var result = [];
            if (typeof data !== 'object' || !Array.isArray(data)) {
                return result;
            }
            for (var i = 0; i < data.length; i++) {
                result[i] = {
                    tag: data[i],
                    filterCategory: filterCategory
                };
            }
            return result;
        }

        /**
         * Converts undefined values to empty arrays, string values to single-element arrays, leaves arrays alone.
         *
         * @param filters
         */
        function sanitizeFilters(filters) {
            switch (typeof filters) {
                case 'undefined':
                    return [];
                case 'string':
                    return new Array(filters);
                default:
                    return filters;
            }
        }

        /**
         * Attempts to add a given filter value to a given filter category.
         *
         * @param filterCategory    The filter category the value belongs to.
         * @param value             The filter being added.
         * @return {boolean}        True if the filter was added (params are valid and if the filter was not already
         *                          applied). False otherwise.
         */
        function addFilter(filterCategory, value) {
            if (!$scope.searchParams.hasOwnProperty(filterCategory)) {
                return false;
            }
            var usedValues = $scope.searchParams[filterCategory];
            var allowedValues = $scope.filters[filterCategory].map(function (element) {
                return element.value;
            });
            if (usedValues.indexOf(value) === -1 && allowedValues.indexOf(value) !== -1) {
                $log.debug('Adding ' + value + ' to ' + filterCategory + ' category');
                $scope.searchParams[filterCategory].push(value);
                $scope.resetPageNumberOnSubmit = true;
                updateFilterTitle(filterCategory);
                return true;
            } else {
                return false;
            }
        }

        function removeFilter(filterCategory, value) {
            if (!$scope.searchParams.hasOwnProperty(filterCategory)) {
                return;
            }
            var index = $scope.searchParams[filterCategory].indexOf(value);
            if (index === -1) {
                return;
            }
            $log.debug('Removing ' + value + ' from ' + filterCategory + ' category');
            $scope.searchParams[filterCategory].splice(index, 1);
            $scope.resetPageNumberOnSubmit = true;
            updateFilterTitle(filterCategory);
        }

        function setUpFilters() {
            if (!$scope.filtersReady) {
                return;
            }
            for (var filterCategory in $scope.filters) {
                if ($scope.filters.hasOwnProperty(filterCategory)) {
                    // Need to create inner closure so 'filterCategory' has the right value - see https://stackoverflow.com/a/23038392/2333689
                    (function (filterCategory) {
                        var filters = $scope.filters[filterCategory];

                        // TODO do this elsewhere, it doesn't belong here
                        $scope.searchParams[filterCategory] = sanitizeFilters($scope.searchParams[filterCategory]);

                        $timeout(function () {
                            updateFilterTitle(filterCategory);      // Because we have to do this manually, do it once on page load
                          var element = angular.element('#' + filterCategory + '-autocomplete');
                            $log.debug('Attempting to find autocomplete element for ' + filterCategory + ': ', element);
                            element.material_chip({
                                data: initialChipData($scope.searchParams[filterCategory], filterCategory),
                                placeholder: element.data('placeholder'),
                                secondaryPlaceholder: element.data('secondary-placeholder'),
                                autocompleteOptions: {
                                    data: arrayToObj(filters),
                                    // Max amount of results that can be shown at once. Default: Infinity.
                                    limit: 20,
                                    minLength: 1 // The minimum length of the input for the autocomplete to start. Default: 1.
                                }
                            });
                        });
                    })(filterCategory);
                }
            }
            // Done adding all filters to view, expand filters collapsible if necessary
            if (hasFilters() && $scope.games) {
                openFiltersCollapsible();
            }
        }

        function hasFilters() {
            return $scope.searchParams.developer.length > 0
                    || $scope.searchParams.publisher.length > 0
                    || $scope.searchParams.genre.length > 0
                    || $scope.searchParams.platform.length > 0;
        }

        /**
         * Out of all filter categories available in the filters collapsible, from left to right as they appear on the
         * page, gets the first category that has at least one value, or defaults to the first.
         */
        function getActiveTab() {
            var categories = ['platform', 'genre', 'developer', 'publisher'];
            if (!hasFilters()) {
                return categories[0];
            }
            return categories.find(function(category) {
                return $scope.searchParams[category].length > 0;
            });
        }

        /**
         * Simulates a click on the appropriate active filter category tab so when the filters collapsible expands, the
         * correct tab is selected.
         */
        function clickActiveTab(timeout) {
            $timeout(function() {
                angular.element('ul.tabs').tabs('select_tab', getActiveTab());
            }, timeout);
        }

        /**
         * Simulates a click on the filters collapsible to open it.
         */
        function openFiltersCollapsible(timeout) {
            timeout = timeout || 100;
            $timeout(function() {
                angular.element('#filters-collapsible').trigger('click');
            }, timeout);
        }

        /**
        * Manual update to the title of a specified fitler tab, to include in parentheses the number of applied filters
         * for a specified category. Attempts to do this via $watchCollection, etc. have failed.
         *
        * @param filterCategory The filter category to update
        */
        function updateFilterTitle(filterCategory) {
          var newCount = $scope.searchParams[filterCategory].length;
          angular.element('#' + filterCategory + '-filter-count').html(newCount > 0 ? (' (' + newCount + ')') : '');
        }

        /* ********************************************
         *          MATERIALIZE INITIALIZATION
         * *******************************************/
        // Collapsible for filters
        angular.element('.collapsible').collapsible();

        // Tab for filter sections
        angular.element('ul.tabs').tabs();

        var firstTime = true;

        angular.element('#filters-collapsible').on('click', function(e) {
            if (firstTime) {
                firstTime = false;
                clickActiveTab();
            }
        });

        // Chips for filters
        angular.forEach(angular.element('.chips'), function(element) {
            element = angular.element(element);

            // Handle delete
            element.on('chip.delete', function (e, chip) {
                removeFilter(chip.filterCategory, chip.tag);
            });

            // Handle add
            element.on('chip.add', function (e, chip) {
              var $target = $(e.target);
              if (!addFilter($target.data('category'), chip.tag)) {
                $target.find('.chip .close:last').trigger('click');  // Trigger clicking on remove button
              }
            });
        });

        $log.debug('Fired Materialize initializers');
    }]);
});
