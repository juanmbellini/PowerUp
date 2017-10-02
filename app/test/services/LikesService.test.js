'use strict';

define(['powerUp', 'angular-mocks', 'restangular', 'AuthService', 'LikesService'], function (a, b, c, d, TestHelper) {

    // What you are testing
    describe('LikesService', function () {

        // Always necessary
        beforeEach(module('powerUp'));

        // All the dependencies for AuthService
        var $httpBackend,   // Used to mock API calls
            $log,           // Everything that the AuthService depends on
            Restangular,
            AuthService,
            LocalStorage,
            LikesService;

        // Other variables
        var user = {id: 1, username: 'paw', email: 'paw@paw.paw'};
        var token = 'blah.blah._blah_-blah';
        var thread = {
            id: 1,
            title: 'To be or not to be',
            body: 'That is the question',
            createdAt: '2016-11-30T02:45:15.260',
            likeCount: 2,
            likedByCurrentUser: null,
            creator: {
                id: 7,
                username: 'lipusal',
                profilePictureUrl: 'http://localhost:8080/api/users/7/picture',
                creatorUrl: 'http://localhost:8080/api/users/7'
            },
            commentsUrl: 'http://localhost:8080/api/threads/1/comments',
            likesUrl: 'http://localhost:8080/api/threads/1/likes'
        };
        var comment = {
            id: 1,
            body: 'According to my mom, to be is the best option',
            createdAt: '2016-11-30T08:55:40.755',
            inReplyTo: null,
            inReplyToUrl: null,
            likeCount: 1,
            likedByCurrentUser: null,
            commenter: {
                id: 7,
                username: 'lipusal',
                profilePictureUrl: 'http://localhost:8080/api/users/7/picture',
                creatorUrl: 'http://localhost:8080/api/users/7'
            },
            repliesUrl: 'http://localhost:8080/api/threads/comments/1/replies',
            likesUrl: 'http://localhost:8080/api/threads/comments/1/likes',
            threadId: 1,
            threadUrl: 'http://localhost:8080/api/threads/1'
        };

        // Have angular inject everything
        beforeEach(inject(function (_$httpBackend_, _$log_, _Restangular_, _localStorageService_, _AuthService_, _LikesService_) {
            $httpBackend = _$httpBackend_;
            $log = _$log_;
            Restangular = _Restangular_;
            LocalStorage = _localStorageService_;
            AuthService = _AuthService_;
            LikesService = _LikesService_;
        }));

        afterEach(function() {
            // Verify we made EXACTLY the number of network requests we expected - not more and not less
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();

            // Clear local storage
            LocalStorage.clearAll();
        });


        describe('#like', function() {
            describe('On invalid calls', function () {
                it('Ignores if not logged in', function () {
                    LikesService.like(Restangular.one('threads', 1));
                    // See afterEach()
                });

                it('Ignores without a restangularized object', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    LikesService.like({});
                });

                it('Ignores when disabled', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    var obj = Object.assign({}, thread, {likesDisabled: true});
                    LikesService.like(obj);
                });

                it('Ignores when already liked', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    var obj = Object.assign({}, thread, {likedByCurrentUser: true});
                    LikesService.like(obj);
                });
            });

            describe('When liking a thread', function() {
                var threadCopy;

                beforeEach(function () {
                    mockLoggedIn();
                    AuthService.trackToken();
                    threadCopy = Object.assign({}, thread);
                    $httpBackend.expectPUT(Restangular.configuration.baseUrl.concat('/threads/' + thread.id + '/likes'), {}, hasAuthorizationHeader).respond(204);
                });

                it('Sets isLikedByCurrentUser to true', function () {
                    LikesService.like(Restangular.one('threads', threadCopy.id), threadCopy);
                    $httpBackend.flush();
                    expect(threadCopy.likedByCurrentUser).toBe(true);
                });

                it('Increments likeCount', function () {
                    var likeCount = threadCopy.likeCount;
                    LikesService.like(Restangular.one('threads', threadCopy.id), threadCopy);
                    $httpBackend.flush();
                    expect(threadCopy.likeCount).toBe(likeCount + 1);
                });

                it('Clears the busy flag when done', function () {
                    LikesService.like(Restangular.one('threads', threadCopy.id), threadCopy);
                    $httpBackend.flush();
                    expect(threadCopy.likesDisabled).toBe(false);
                });
            });

            // TODO test comments?
        });

        describe('#unlike', function() {
            describe('On invalid calls', function () {
                it('Ignores if not logged in', function () {
                    LikesService.unlike(Restangular.one('threads', 1));
                    // See afterEach()
                });

                it('Ignores without a restangularized object', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    LikesService.unlike({});
                });

                it('Ignores when disabled', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    var obj = Object.assign({}, comment, {likesDisabled: true});
                    LikesService.unlike(obj);
                });

                it('Ignores when not liked', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    var obj = Object.assign({}, comment, {likedByCurrentUser: false});
                    LikesService.unlike(obj);
                });
            });

            describe('When unliking a comment', function() {
                var commentCopy;

                beforeEach(function () {
                    mockLoggedIn();
                    AuthService.trackToken();
                    commentCopy = Object.assign({}, comment);
                    $httpBackend.expectDELETE(Restangular.configuration.baseUrl.concat('/threads/comments/' + comment.id + '/likes'), hasAuthorizationHeader).respond(204);
                });

                it('Sets isLikedByCurrentUser to false', function () {
                    LikesService.unlike(Restangular.all('threads').one('comments', commentCopy.id), commentCopy);
                    $httpBackend.flush();
                    expect(commentCopy.likedByCurrentUser).toBe(false);
                });

                it('Decrements likeCount', function () {
                    var likeCount = commentCopy.likeCount;
                    LikesService.unlike(Restangular.all('threads').one('comments', commentCopy.id), commentCopy);
                    $httpBackend.flush();
                    expect(commentCopy.likeCount).toBe(likeCount - 1);
                });

                it('Clears the busy flag when done', function () {
                    LikesService.unlike(Restangular.all('threads').one('comments', commentCopy.id), commentCopy);
                    $httpBackend.flush();
                    expect(commentCopy.likesDisabled).toBe(false);
                });
            });

            // TODO test threads?
        });

        describe('#isLikedByCurrentUser', function() {
            describe('On invalid calls', function() {
                it('Ignores if not logged in', function () {
                    expect(LikesService.isLikedByCurrentUser(Object.assign({likedByCurrentUser: true}, thread))).toBe(false);
                    // Also see afterEach()
                });

                it('Ignores with an invalid object', function () {
                    mockLoggedIn(); // Make sure it doesn't fail because we're not logged in
                    expect(LikesService.isLikedByCurrentUser({})).toBe(false);
                });
            });

            describe('On valid call', function() {
                beforeEach(mockLoggedIn);

                it('Returns true when liked', function() {
                    expect(LikesService.isLikedByCurrentUser(Object.assign({}, thread, {likedByCurrentUser: true}))).toBe(true);
                });

                it('Returns false when not liked', function() {
                    expect(LikesService.isLikedByCurrentUser(Object.assign({}, thread, {likedByCurrentUser: false}))).toBe(false);
                });

                it('Returns null when unknown', function() {
                    expect(LikesService.isLikedByCurrentUser(Object.assign({}, thread, {likedByCurrentUser: null}))).toBeNull();
                });
            });
        });

        /**
         * Mocks user and JWT stored in local storage
         */
        function mockLoggedIn() {
            LocalStorage.set('currentUser', user);
            LocalStorage.set('jwt', token);
        }

        function hasAuthorizationHeader(headers) {
            return headers.hasOwnProperty('Authorization');
        }
    });
});
