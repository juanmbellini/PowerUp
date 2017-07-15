'use strict';
define(['powerUp', 'paginationService'], function(powerUp) {

    powerUp.service('FeedService', ['$log', 'Restangular', 'AuthService', 'PaginationService', function($log, Restangular, AuthService, PaginationService) {
// TODO limpiar lo que no se usa
        Restangular.setFullResponse(true);

        function initialize(initialUserId) {
            var feedProviders = [];
            var userId = initialUserId;
            // var threadsFeedPaginator = PaginationService.initialize(Restangular.one('users',userId));
            // var reviewsFeedPaginator = PaginationService.initialize(Restangular.one('users',userId));
            // var statusFeedPaginator = PaginationService.initialize(Restangular.one('users',userId));
            // TODO usar los verdadeors paginators

            // threads
            var threadFeedProvider = {};
            threadFeedProvider.array = [];
            threadFeedProvider.pointer = 0;
            threadFeedProvider.paginator = PaginationService.initialize(Restangular.all('threads'));
            threadFeedProvider.type = 'thread';
            threadFeedProvider.dateName = 'createdAt';
            feedProviders.push(threadFeedProvider);

            // reviews
            var reviewFeedProvider = {};
            reviewFeedProvider.array = [];
            reviewFeedProvider.pointer = 0;
            reviewFeedProvider.paginator = PaginationService.initialize(Restangular.all('reviews'));
            reviewFeedProvider.type = 'review';
            reviewFeedProvider.dateName = 'date';
            feedProviders.push(reviewFeedProvider);

            feedProviders.forEach(function (providerCopy, index, array) {
                var feedProvider = array[index];
                PaginationService.getPage(feedProvider.paginator, 1, function(response) {
                    var array = response.data;
                    array.forEach(function (element, array, index) {
                        feedProvider.array.push(element);
                    });
                    feedProvider.isReady = true;
                }, function(error) {
                    $log.error('Error getting nextPage: ', error);
                    feedProvider.isReady = true;
                    // TODO do something? Stop asking for these pages???
                });
            });

            var feedObject = {};
            feedObject.feedProviders = feedProviders;
            feedObject.userId = userId;
            // feedProviders.push(statusFeedProvider);
            return feedObject;
        }

        function getMore(feedProvider) {
            if (!hasMore(feedProvider)) {
                feedProvider.isReady = true;
                return;
            }
            feedProvider.isReady = false;
            PaginationService.getNextPage(feedProvider.paginator, function(response) {
                var array = response.data;
                array.forEach(function (element, array, index) {
                   feedProvider.array.push(element);
                });
                feedProvider.isReady = true;
            }, function(error) {
                $log.error('Error getting nextPage: ', error);
                feedProvider.isReady = true;
                // TODO do something? Stop asking for these pages???
            });
        }

        function isReady(feedObj) {
            if (feedObj === null || angular.isUndefined(feedObj)) {
                return false;
            }
            var feedProviders = feedObj.feedProviders;
            var isItReady = true;
            feedProviders.forEach(function (providerCopy, index, array) {
                if (!providerCopy.isReady || providerCopy.array.length <= 0) {
                    isItReady = false;
                }
            });
            return isItReady;
        }

        function getFeedElement(feedObj) {
            var feedProviders = feedObj.feedProviders;
            if (!isReady(feedObj)) {
                return null;
            }
            var newestElementProvider = null;
            var newestDate = null;
            feedProviders.forEach(function (providerCopy, index, array) {
                var element = peekNext(providerCopy);
                if (element !== null) {
                    var newDate = new Date(element[providerCopy.dateName]);
                    if (newestDate === null) {
                        newestDate = newDate;
                        newestElementProvider = array[index];
                    }
                    if (newDate > newestDate) {
                        newestDate = newDate;
                        newestElementProvider = array[index];
                    }
                }
            });
            return getNext(newestElementProvider);
        }

        function peekNext(feedProvider) {
            if (!hasMore(feedProvider) || !feedProvider.isReady) {
                return null;
            }
            return feedProvider.array[feedProvider.pointer];
        }

        function getNext(feedProvider) {
            if (!hasMore(feedProvider) || !feedProvider.isReady) {
                return null;
            }
            var element = feedProvider.array[feedProvider.pointer];
            feedProvider.pointer++;
            if (feedProvider.pointer >= feedProvider.array.length) {
                getMore(feedProvider);
            }
            var wrapper = {};
            wrapper.data = element;
            wrapper.type = feedProvider.type;
            return wrapper;
        }

        function hasMore(feedProvider) {
            return (PaginationService.hasMorePages(feedProvider.paginator) || feedProvider.pointer < feedProvider.array.length);
        }

        return {
            initialize: initialize,
            getFeedElement: getFeedElement,
            isReady: isReady
        };
    }]);
});
