'use strict';
define(['powerUp', 'PaginationService'], function(powerUp) {

    powerUp.service('FeedService', ['$log', 'Restangular', 'AuthService', 'PaginationService', function($log, Restangular, AuthService, PaginationService) {
// TODO limpiar lo que no se usa
        Restangular.setFullResponse(true);

        function initialize(initialUserId) {
            var feedProviders = [];
            var userId = initialUserId;

            // threads
            var threadFeedProvider = {};
            threadFeedProvider.array = [];
            threadFeedProvider.pointer = 0;
            threadFeedProvider.paginator = PaginationService.initialize(Restangular.one('users',userId).all('feed').all('threads'), undefined, undefined, 10);
            threadFeedProvider.type = 'thread';
            threadFeedProvider.dateName = 'createdAt';
            feedProviders.push(threadFeedProvider);

            // reviews
            var reviewFeedProvider = {};
            reviewFeedProvider.array = [];
            reviewFeedProvider.pointer = 0;
            reviewFeedProvider.paginator = PaginationService.initialize(Restangular.one('users',userId).all('feed').all('reviews'), undefined, undefined, 10);
            reviewFeedProvider.type = 'review';
            reviewFeedProvider.dateName = 'date';
            feedProviders.push(reviewFeedProvider);

            // status
            var statusFeedProvider = {};
            statusFeedProvider.array = [];
            statusFeedProvider.pointer = 0;
            statusFeedProvider.paginator = PaginationService.initialize(Restangular.one('users',userId).all('feed').all('statuses'), undefined, undefined, 10);
            statusFeedProvider.type = 'status';
            statusFeedProvider.dateName = 'date';
            feedProviders.push(statusFeedProvider);

            feedProviders.forEach(function (providerCopy, index, array) {
                var feedProvider = array[index];
                feedProvider.isReady = false;
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
            feedObject.thereAreMore = true;
            // feedProviders.push(statusFeedProvider);
            return feedObject;
        }

        function getMore(feedProvider) {
            if (!hasMore(feedProvider)) {
                feedProvider.isReady = true;
                return;
            }
            feedProvider.isReady = false;
            if (PaginationService.hasMorePages(feedProvider.paginator) && feedProvider.array.length < feedProvider.pointer + 15) {
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
            } else {
                feedProvider.isReady = true;
            }
        }

        function isReady(feedObj) {
            if (feedObj === null || angular.isUndefined(feedObj)) {
                return false;
            }
            var feedProviders = feedObj.feedProviders;
            var isItReady = true;
            feedProviders.forEach(function (providerCopy, index, array) {
                if (!providerCopy.isReady) {
                    isItReady = false;
                }
            });
            return isItReady;
        }

        function getFeed(feedObj) {
            if (feedObj === null || !isReady(feedObj)) {
                return [];
            }
            var feedProviders = feedObj.feedProviders;
            var thereAreMore = true;
            var feedArray = [];
            var counter = 0;
            while (thereAreMore && counter < 60) {
                var newestElementProvider = null;
                var newestDate = null;
                counter = counter + 1;
                feedProviders.forEach(function (providerCopy, index, array) {
                    if (thereAreMore) {
                        if (hasMore(providerCopy)) {
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
                            } else {
                                thereAreMore = false;
                            }
                        }
                    }
                });
                if (thereAreMore) {
                    if (newestElementProvider === null) {
                        thereAreMore = false;
                        feedObj.thereAreMore = false;
                    } else {
                        feedArray.push(getNext(newestElementProvider));
                    }
                }
            }
            feedProviders.forEach(function (providerCopy, index, array) {
                getMore(array[index]);
            });
            return feedArray;
        }

        function peekNext(feedProvider) {
            if (feedProvider.pointer >= feedProvider.array.length) {
                return null;
            }
            return feedProvider.array[feedProvider.pointer];
        }

        function getNext(feedProvider) {
            var element = peekNext(feedProvider);
            if (element === null) {
                return null;
            }
            feedProvider.pointer++;
            var wrapper = {};
            wrapper.data = element;
            wrapper.type = feedProvider.type;
            if (wrapper.type === 'review') {
                Restangular.one('users', element.userId).all('game-scores').getList({gameId: element.gameId}).then(function (response) {
                    var gameScore = response.data;
                    if (gameScore.length > 0) {
                        element.overallScore = gameScore[0].score;
                    }
                });
                Restangular.one('users',element.userId).all('shelves').getList({gameId: element.gameId}).then(function (response) {
                    var shelvesWithGame = response.data;
                    element.shelves = shelvesWithGame;
                });
            }
            return wrapper;
        }

        function hasMore(feedProvider) {
            return (PaginationService.hasMorePages(feedProvider.paginator) || feedProvider.pointer < feedProvider.array.length);
        }

        return {
            initialize: initialize,
            getFeed: getFeed,
            isReady: isReady
        };
    }]);
});
