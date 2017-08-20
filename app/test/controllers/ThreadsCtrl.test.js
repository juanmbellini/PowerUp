'use strict';

define(['powerUp', 'angular-mocks', 'AuthService', 'LikesService', 'PaginationService', 'ThreadsCtrl'], function () {

    describe('ThreadsCtrl', function () {

        beforeEach(module('powerUp'));

        var $httpBackend,   // Used to mock API calls
            $rootScope,     // Used to create a $scope
            $scope,
            $location,
            $log,
            Restangular,
            AuthService,
            LikesService,
            PaginationService,
            ThreadsCtrl;

        // Other variables
        // var user = {id: 1, username: "paw", email: "paw@paw.paw"};

        // Injection
        beforeEach(inject(function (_$httpBackend_, _$rootScope_, _$log_, _$location_, _Restangular_, _AuthService_, _LikesService_, _PaginationService_, _$controller_) {
            $httpBackend = _$httpBackend_;
            $rootScope = _$rootScope_;
            $scope = $rootScope.$new();     // Actually create a $scope
            $location = _$location_;
            $log = _$log_;
            Restangular = _Restangular_;
            AuthService = _AuthService_;
            LikesService = _LikesService_;
            PaginationService = _PaginationService_;
            // Actually create the controller, passing all the dependencies to it
            ThreadsCtrl = _$controller_('ThreadsCtrl', {
                $scope: $scope,
                $location: $location,
                $log: $log,
                Restangular: Restangular,
                AuthService: AuthService,
                LikesService: LikesService,
                PaginationService: PaginationService
            });

            // Return threads when querying
            var regex = new RegExp(Restangular.configuration.baseUrl.concat('/threads.*'));
            $httpBackend.whenGET(regex).respond(threads);
        }));

        afterEach(function() {
            // Verify we made EXACTLY the number of network requests we expected - not more and not less
            $httpBackend.verifyNoOutstandingExpectation();
            $httpBackend.verifyNoOutstandingRequest();
        });



        /*
         * Test the isLoggedIn function. You can organize your tests however you want (e.g. before I was testing when
         * the user was logged in and when the user was not logged in, but I thought it would be better organized if I
         * grouped by method), but each test group goes under a "describe" block.
         */
        describe('#isLikedByCurrentUser', function () {
            describe('When not logged in', function () {
                it('Should always return false', function () {
                    expect($scope.isLikedByCurrentUser()).toBe(false);
                    expect($scope.isLikedByCurrentUser(/*TODO pass a thread*/)).toBe(false);
                });
            });
        });

        // TODO test more functions

        // TODO test API stuff with $httpBackend

        var threads = [{
            'id': 1,
            'title': 'To be or not to be',
            'body': 'That is the question',
            'createdAt': '2016-11-30T02:45:15.260',
            'commentCount': 2,
            'likeCount': 2,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/1/comments',
            'likesUrl': 'http://localhost:8080/api/threads/1/likes'
        }, {
            'id': 2,
            'title': 'Best MMO?',
            'body': '',
            'createdAt': '2016-11-30T09:57:41.489',
            'commentCount': 1,
            'likeCount': 3,
            'likedByCurrentUser': null,
            'creator': {
                'id': 1,
                'username': 'paw',
                'profilePictureUrl': 'http://localhost:8080/api/users/1/picture',
                'creatorUrl': 'http://localhost:8080/api/users/1'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/2/comments',
            'likesUrl': 'http://localhost:8080/api/threads/2/likes'
        }, {
            'id': 3,
            'title': 'Chrono Trigger vs Dragon Ball Z',
            'body': 'Awante akira toriyama 鳥山 明',
            'createdAt': '2016-11-30T18:51:58.686',
            'commentCount': 1,
            'likeCount': 4,
            'likedByCurrentUser': null,
            'creator': {
                'id': 1,
                'username': 'paw',
                'profilePictureUrl': 'http://localhost:8080/api/users/1/picture',
                'creatorUrl': 'http://localhost:8080/api/users/1'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/3/comments',
            'likesUrl': 'http://localhost:8080/api/threads/3/likes'
        }, {
            'id': 4,
            'title': 'sdfsdf',
            'body': 'sdfsdf',
            'createdAt': '2016-11-30T19:11:57.221',
            'commentCount': 1,
            'likeCount': 0,
            'likedByCurrentUser': null,
            'creator': {
                'id': 2,
                'username': 'droche',
                'profilePictureUrl': 'http://localhost:8080/api/users/2/picture',
                'creatorUrl': 'http://localhost:8080/api/users/2'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/4/comments',
            'likesUrl': 'http://localhost:8080/api/threads/4/likes'
        }, {
            'id': 5,
            'title': 'The time of your life',
            'body': 'Lol Idunno why I even wrote this thread',
            'createdAt': '2016-11-30T19:34:55.492',
            'commentCount': 0,
            'likeCount': 2,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/5/comments',
            'likesUrl': 'http://localhost:8080/api/threads/5/likes'
        }, {
            'id': 6,
            'title': 'Did you really know?',
            'body': 'Forty-two is a pronic number[1] and an abundant number; its prime factorization 2 · 3 · 7 makes it the second sphenic number and also the second of the form (2 · 3 · r). As with all sphenic numbers of this form, the aliquot sum is abundant by 12. 42 is also the second sphenic number to be bracketed by twin primes; 30 is also a pronic number and also rests between two primes. 42 has a 14-member aliquot sequence 42, 54, 66, 78, 90, 144, 259, 45, 33, 15, 9, 4, 3, 1, 0 and is itself part of the aliquot sequence commencing with the first sphenic number 30. Further, 42 is the 10th member of the 3-aliquot tree. Additional properties of the number 42 include:\n\nIt is the third primary pseudoperfect number.[2]\nIt is a Catalan number.[3] Consequently, 42 is the number of noncrossing partitions of a set of five elements, the number of triangulations of a heptagon, the number of rooted ordered binary trees with six leaves, the number of ways in which five pairs of nested parentheses can be arranged, etc.\nIt is an alternating sign matrix number, that is, the number of 4-by-4 alternating sign matrices.\nIt is the number of partitions of 10—the number of ways of expressing 10 as a sum of positive integers (note a different sense of partition from that above). jkhkjh',
            'createdAt': '2016-11-30T19:35:40.045',
            'commentCount': 2,
            'likeCount': 0,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/6/comments',
            'likesUrl': 'http://localhost:8080/api/threads/6/likes'
        }, {
            'id': 7,
            'title': 'First 100 prime numbers',
            'body': '2\t3\t5\t7\t11\t13\t17\t19\t23\t29\n31\t37\t41\t43\t47\t53\t59\t61\t67\t71\n73\t79\t83\t89\t97\t101\t103\t107\t109\t113\n127\t131\t137\t139\t149\t151\t157\t163\t167\t173\n179\t181\t191\t193\t197\t199\t211\t223\t227\t229\n233\t239\t241\t251\t257\t263\t269\t271\t277\t281\n283\t293\t307\t311\t313\t317\t331\t337\t347\t349\n353\t359\t367\t373\t379\t383\t389\t397\t401\t409\n419\t421\t431\t433\t439\t443\t449\t457\t461\t463\n467\t479\t487\t491\t499\t503\t509\t521\t523\t541\n547\t557\t563\t569\t571\t577\t587\t593\t599\t601\n607\t613\t617\t619\t631\t641\t643\t647\t653\t659\n661\t673\t677\t683\t691\t701\t709\t719\t727\t733\n739\t743\t751\t757\t761\t769\t773\t787\t797\t809\n811\t821\t823\t827\t829\t839\t853\t857\t859\t863\n877\t881\t883\t887\t907\t911\t919\t929\t937\t941\n947\t953\t967\t971\t977\t983\t991\t997\t1009\t1013\n1019\t1021\t1031\t1033\t1039\t1049\t1051\t1061\t1063\t1069\n1087\t1091\t1093\t1097\t1103\t1109\t1117\t1123\t1129\t1151\n1153\t1163\t1171\t1181\t1187\t1193\t1201\t1213\t1217\t1223\n1229\t1231\t1237\t1249\t1259\t1277\t1279\t1283\t1289\t1291\n1297\t1301\t1303\t1307\t1319\t1321\t1327\t1361\t1367\t1373\n1381\t1399\t1409\t1423\t1427\t1429\t1433\t1439\t1447\t1451\n1453\t1459\t1471\t1481\t1483\t1487\t1489\t1493\t1499\t1511\n1523\t1531\t1543\t1549\t1553\t1559\t1567\t1571\t1579\t1583\n1597\t1601\t1607\t1609\t1613\t1619\t1621\t1627\t1637\t1657\n1663\t1667\t1669\t1693\t1697\t1699\t1709\t1721\t1723\t1733\n1741\t1747\t1753\t1759\t1777\t1783\t1787\t1789\t1801\t1811\n1823\t1831\t1847\t1861\t1867\t1871\t1873\t1877\t1879\t1889\n1901\t1907\t1913\t1931\t1933\t1949\t1951\t1973\t1979\t1987\n1993\t1997\t1999\t2003\t2011\t2017\t2027\t2029\t2039\t2053\n2063\t2069\t2081\t2083\t2087\t2089\t2099\t2111\t2113\t2129\n2131\t2137\t2141\t2143\t2153\t2161\t2179\t2203\t2207\t2213\n2221\t2237\t2239\t2243\t2251\t2267\t2269\t2273\t2281\t2287\n2293\t2297\t2309\t2311\t2333\t2339\t2341\t2347\t2351\t2357\n2371\t2377\t2381\t2383\t2389\t2393\t2399\t2411\t2417\t2423\n2437\t2441\t2447\t2459\t2467\t2473\t2477\t2503\t2521\t2531\n2539\t2543\t2549\t2551\t2557\t2579\t2591\t2593\t2609\t2617\n2621\t2633\t2647\t2657\t2659\t2663\t2671\t2677\t2683\t2687\n2689\t2693\t2699\t2707\t2711\t2713\t2719\t2729\t2731\t2741\n2749\t2753\t2767\t2777\t2789\t2791\t2797\t2801\t2803\t2819\n2833\t2837\t2843\t2851\t2857\t2861\t2879\t2887\t2897\t2903\n2909\t2917\t2927\t2939\t2953\t2957\t2963\t2969\t2971\t2999\n3001\t3011\t3019\t3023\t3037\t3041\t3049\t3061\t3067\t3079\n3083\t3089\t3109\t3119\t3121\t3137\t3163\t3167\t3169\t3181\n3187\t3191\t3203\t3209\t3217\t3221\t3229\t3251\t3253\t3257\n3259\t3271\t3299\t3301\t3307\t3313\t3319\t3323\t3329\t3331\n3343\t3347\t3359\t3361\t3371\t3373\t3389\t3391\t3407\t3413\n3433\t3449\t3457\t3461\t3463\t3467\t3469\t3491\t3499\t3511\n3517\t3527\t3529\t3533\t3539\t3541\t3547\t3557\t3559\t3571\n3581\t3583\t3593\t3607\t3613\t3617\t3623\t3631\t3637\t3643\n3659\t3671\t3673\t3677\t3691\t3697\t3701\t3709\t3719\t3727\n3733\t3739\t3761\t3767\t3769\t3779\t3793\t3797\t3803\t3821\n3823\t3833\t3847\t3851\t3853\t3863\t3877\t3881\t3889\t3907\n3911\t3917\t3919\t3923\t3929\t3931\t3943\t3947\t3967\t3989\n4001\t4003\t4007\t4013\t4019\t4021\t4027\t4049\t4051\t4057\n4073\t4079\t4091\t4093\t4099\t4111\t4127\t4129\t4133\t4139\n4153\t4157\t4159\t4177\t4201\t4211\t4217\t4219\t4229\t4231\n4241\t4243\t4253\t4259\t4261\t4271\t4273\t4283\t4289\t4297\n4327\t4337\t4339\t4349\t4357\t4363\t4373\t4391\t4397\t4409\n4421\t4423\t4441\t4447\t4451\t4457\t4463\t4481\t4483\t4493\n4507\t4513\t4517\t4519\t4523\t4547\t4549\t4561\t4567\t4583\n4591\t4597\t4603\t4621\t4637\t4639\t4643\t4649\t4651\t4657\n4663\t4673\t4679\t4691\t4703\t4721\t4723\t4729\t4733\t4751\n4759\t4783\t4787\t4789\t4793\t4799\t4801\t4813\t4817\t4831\n4861\t4871\t4877\t4889\t4903\t4909\t4919\t4931\t4933\t4937\n4943\t4951\t4957\t4967\t4969\t4973\t4987\t4993\t4999\t5003\n5009\t5011\t5021\t5023\t5039\t5051\t5059\t5077\t5081\t5087\n5099\t5101\t5107\t5113\t5119\t5147\t5153\t5167\t5171\t5179\n5189\t5197\t5209\t5227\t5231\t5233\t5237\t5261\t5273\t5279\n5281\t5297\t5303\t5309\t5323\t5333\t5347\t5351\t5381\t5387\n5393\t5399\t5407\t5413\t5417\t5419\t5431\t5437\t5441\t5443\n5449\t5471\t5477\t5479\t5483\t5501\t5503\t5507\t5519\t5521\n5527\t5531\t5557\t5563\t5569\t5573\t5581\t5591\t5623\t5639\n5641\t5647\t5651\t5653\t5657\t5659\t5669\t5683\t5689\t5693\n5701\t5711\t5717\t5737\t5741\t5743\t5749\t5779\t5783\t5791\n5801\t5807\t5813\t5821\t5827\t5839\t5843\t5849\t5851\t5857\n5861\t5867\t5869\t5879\t5881\t5897\t5903\t5923\t5927\t5939\n5953\t5981\t5987\t6007\t6011\t6029\t6037\t6043\t6047\t6053\n6067\t6073\t6079\t6089\t6091\t6101\t6113\t6121\t6131\t6133\n6143\t6151\t6163\t6173\t6197\t6199\t6203\t6211\t6217\t6221\n6229\t6247\t6257\t6263\t6269\t6271\t6277\t6287\t6299\t6301\n6311\t6317\t6323\t6329\t6337\t6343\t6353\t6359\t6361\t6367\n6373\t6379\t6389\t6397\t6421\t6427\t6449\t6451\t6469\t6473\n6481\t6491\t6521\t6529\t6547\t6551\t6553\t6563\t6569\t6571\n6577\t6581\t6599\t6607\t6619\t6637\t6653\t6659\t6661\t6673\n6679\t6689\t6691\t6701\t6703\t6709\t6719\t6733\t6737\t6761\n6763\t6779\t6781\t6791\t6793\t6803\t6823\t6827\t6829\t6833\n6841\t6857\t6863\t6869\t6871\t6883\t6899\t6907\t6911\t6917\n6947\t6949\t6959\t6961\t6967\t6971\t6977\t6983\t6991\t6997\n7001\t7013\t7019\t7027\t7039\t7043\t7057\t7069\t7079\t7103\n7109\t7121\t7127\t7129\t7151\t7159\t7177\t7187\t7193\t7207\n7211\t7213\t7219\t7229\t7237\t7243\t7247\t7253\t7283\t7297\n7307\t7309\t7321\t7331\t7333\t7349\t7351\t7369\t7393\t7411\n7417\t7433\t7451\t7457\t7459\t7477\t7481\t7487\t7489\t7499\n7507\t7517\t7523\t7529\t7537\t7541\t7547\t7549\t7559\t7561\n7573\t7577\t7583\t7589\t7591\t7603\t7607\t7621\t7639\t7643\n7649\t7669\t7673\t7681\t7687\t7691\t7699\t7703\t7717\t7723\n7727\t7741\t7753\t7757\t7759\t7789\t7793\t7817\t7823\t7829\n7841\t7853\t7867\t7873\t7877\t7879\t7883\t7901\t7907\t7919\n7927\t7933\t7937\t7949\t7951\t7963\t7993\t8009\t8011\t8017\n8039\t8053\t8059\t8069\t8081\t8087\t8089\t8093\t8101\t8111\n8117\t8123\t8147\t8161\t8167\t8171\t8179\t8191\t8209\t8219\n8221\t8231\t8233\t8237\t8243\t8263\t8269\t8273\t8287\t8291\n8293\t8297\t8311\t8317\t8329\t8353\t8363\t8369\t8377\t8387\n8389\t8419\t8423\t8429\t8431\t8443\t8447\t8461\t8467\t8501\n8513\t8521\t8527\t8537\t8539\t8543\t8563\t8573\t8581\t8597\n8599\t8609\t8623\t8627\t8629\t8641\t8647\t8663\t8669\t8677\n8681\t8689\t8693\t8699\t8707\t8713\t8719\t8731\t8737\t8741\n8747\t8753\t8761\t8779\t8783\t8803\t8807\t8819\t8821\t8831\n8837\t8839\t8849\t8861\t8863\t8867\t8887\t8893\t8923\t8929\n8933\t8941\t8951\t8963\t8969\t8971\t8999\t9001\t9007\t9011\n9013\t9029\t9041\t9043\t9049\t9059\t9067\t9091\t9103\t9109\n9127\t9133\t9137\t9151\t9157\t9161\t9173\t9181\t9187\t9199\n9203\t9209\t9221\t9227\t9239\t9241\t9257\t9277\t9281\t9283\n9293\t9311\t9319\t9323\t9337\t9341\t9343\t9349\t9371\t9377\n9391\t9397\t9403\t9413\t9419\t9421\t9431\t9433\t9437\t9439\n9461\t9463\t9467\t9473\t9479\t9491\t9497\t9511\t9521\t9533\n9539\t9547\t9551\t9587\t9601\t9613\t9619\t9623\t9629\t9631\n9643\t9649\t9661\t9677\t9679\t9689\t9697\t9719\t9721\t9733\n9739\t9743\t9749\t9767\t9769\t9781\t9787\t9791\t9803\t9811\n9817\t9829\t9833\t9839\t9851\t9857\t9859\t9871\t9883\t9887\n9901\t9907\t9923\t9929\t9931\t9941\t9949\t9967\t9973',
            'createdAt': '2016-11-30T19:36:14.586',
            'commentCount': 0,
            'likeCount': 0,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/7/comments',
            'likesUrl': 'http://localhost:8080/api/threads/7/likes'
        }, {
            'id': 8,
            'title': 'African Engine Update Notes',
            'body': "Ok guy's it's UPDATE TIME!! PLEASE READ ALL OF THIS (VERY IMPORTANT!!!!) (A lot has changed)\n\nIP PROTECTION IS HERE!!!\n\n1. We have rewritten the object attachment protection (it will be a lot more accurate when telling you who attached things to you!)\n\n2. We now cater for GoD copies of the game when it comes to .ini, .afc files etc.. If you run a GoD copy then an 'African Funny Cars' folder will be created in the same directory as your NiNJA.xex\n same as GTAVMenuSettings.ini (It will be created in the same directory as NiNJA.xex for GoD users), if you wish to use the full object list, yep you guessed it, it also goes in the same directory as your NiNJA.xex if you are using a GoD copy.\n\nIF YOU DO NOT USE A GAME ON DEMAND COPY THEN EVERYTHING WILL BE DONE IN YOUR MAIN GTA FOLDER!!\n\n3. We now have an .ini that is created by African Engine, by default god mode, never wanted, vehicle god mode loop, and ip protection are all turned on, you can turn them off in the GTAVMenuSettings.ini which will be located in your GTA V Game folder or for GoD in the same directory as NiNJA.xex\n\n4. We have added IP protection (Turned on in GTAVMenuSettings.ini), if you are having problems connecting to games then sign out and sign back in and try again, if that doesn't work then turn off IP protection in the GTAVMenuSettings.ini, this MAY cause problems connecting / disconnecting from games sometimes.\n\n5. We have added console freeze protection, when people try freeze you with the known methods (example havoc's 'Crash Console' option it will not crash you and it will tell you who is trying to crash you!\n\n6. Full vehicle spawner for client's in 'Nice Options' in the online player menu.\n\nTo keep up to date, check out our facebook group:\nhttps://www.facebook.com/groups/africangtaengine/",
            'createdAt': '2016-11-30T19:37:00.765',
            'commentCount': 0,
            'likeCount': 1,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/8/comments',
            'likesUrl': 'http://localhost:8080/api/threads/8/likes'
        }, {
            'id': 9,
            'title': 'Best Heroes Of Might And Magic game?',
            'body': 'My opinion is that Heroes of Might & Magic III is the best, no doubt. But I just wondered what all of you think, and why?',
            'createdAt': '2016-11-30T19:37:20.131',
            'commentCount': 0,
            'likeCount': 2,
            'likedByCurrentUser': null,
            'creator': {
                'id': 17,
                'username': 'asdfgh',
                'profilePictureUrl': 'http://localhost:8080/api/users/17/picture',
                'creatorUrl': 'http://localhost:8080/api/users/17'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/9/comments',
            'likesUrl': 'http://localhost:8080/api/threads/9/likes'
        }, {
            'id': 10,
            'title': 'Get Fit!',
            'body': 'THE FITNESS GRAM PACER TEST IS A MULTISTAGE AEROBIC CAPACITY TEST THAT PROGRESSIVELY GETS MORE DIFFICULT AS IT CONTINUES. THE 20 METER PACER TEST WILL BEGIN IN 30 SECONDS. LINE UP AT THE START. THE RUNNING SPEED STARTS SLOWLY BUT GETS FASTER EACH MINUTE AFTER YOU HEAR THIS SIGNAL. SIGNAL A SINGLE LAP SHOULD BE COMPLETED AFTER YOU HEAR THIS SOUND SOUND  REMEMBER TO RUN IN A STRAIGHT LINE AND RUN AS LONG AS POSSIBLE. THE SECOND TIME YOU FAIL TO COMPLETE A LAP BEFORE THE SOUND, YOUR TEST IS OVER. THE TEST WILL BEGIN ON THE WORD START. ON YOUR MARK. GET READY. START.',
            'createdAt': '2016-11-30T19:37:37.246',
            'commentCount': 0,
            'likeCount': 0,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/10/comments',
            'likesUrl': 'http://localhost:8080/api/threads/10/likes'
        }, {
            'id': 11,
            'title': 'Whois Google?',
            'body': "Aborting search 50 records found .....\nGOOGLE.COM.ACKNOWLEDGES.NON-FREE.COM\nGOOGLE.COM.AFRICANBATS.ORG\nGOOGLE.COM.ANGRYPIRATES.COM\nGOOGLE.COM.AR\nGOOGLE.COM.AU\nGOOGLE.COM.BAISAD.COM\nGOOGLE.COM.BEYONDWHOIS.COM\nGOOGLE.COM.BR\nGOOGLE.COM.BUGBOUNTY.TEST.CIPRI.COM\nGOOGLE.COM.CN\nGOOGLE.COM.CO\nGOOGLE.COM.DEADKNIFERECORDS.COM\nGOOGLE.COM.DGJTEST028-PP-QM-STG.COM\nGOOGLE.COM.DIGNITYPRODUCT.COM\nGOOGLE.COM.DO\nGOOGLE.COM.EG\nGOOGLE.COM.FORSALE\nGOOGLE.COM.HACKED.BY.JAPTRON.ES\nGOOGLE.COM.HANNAHJESSICA.COM\nGOOGLE.COM.HAS.LESS.FREE.PORN.IN.ITS.SEARCH.ENGINE.THAN.SECZY.COM\nGOOGLE.COM.HK\nGOOGLE.COM.HOUDA.DO.YOU.WANT.TO.MARRY.ME.JEN.RE\nGOOGLE.COM.IS.APPROVED.BY.NUMEA.COM\nGOOGLE.COM.IS.NOT.HOSTED.BY.ACTIVEDOMAINDNS.NET\nGOOGLE.COM.LASERPIPE.COM.DOMAINPENDINGDELETE.COM\nGOOGLE.COM.LOLOLOLOLOL.SHTHEAD.COM\nGOOGLE.COM.MAIKO.BE\nGOOGLE.COM.MX\nGOOGLE.COM.MY\nGOOGLE.COM.NOHAREKART.COM\nGOOGLE.COM.NS1.CHALESHGAR.COM\nGOOGLE.COM.NS2.CHALESHGAR.COM\nGOOGLE.COM.PE\nGOOGLE.COM.PK\nGOOGLE.COM.SA\nGOOGLE.COM.SG\nGOOGLE.COM.SHQIPERIA.COM\nGOOGLE.COM.SOUTHBEACHNEEDLEARTISTRY.COM\nGOOGLE.COM.SPAMMING.IS.UNETHICAL.PLEASE.STOP.THEM.HUAXUEERBAN.COM\nGOOGLE.COM.SPROSIUYANDEKSA.RU\nGOOGLE.COM.SUCKS.FIND.CRACKZ.WITH.SEARCH.GULLI.COM\nGOOGLE.COM.TESTZZZZ.3000-RI.COM.DELETE-DNS.COM\nGOOGLE.COM.TR\nGOOGLE.COM.TW\nGOOGLE.COM.UA\nGOOGLE.COM.UK\nGOOGLE.COM.UY\nGOOGLE.COM.VABDAYOFF.COM\nGOOGLE.COM.VN\nGOOGLE.COM\n\nTo single out one record, look it up with \"xxx\", where xxx is one of the\nrecords displayed above. If the records are the same, look them up\nwith \"=xxx\" to receive a full display for each record.\n\n>>> Last update of whois database: Wed, 30 Nov 2016 22:38:15 GMT <<<\n\nFor more information on Whois status codes, please visit https://icann.org/epp\n\nNOTICE: The expiration date displayed in this record is the date the\nregistrar's sponsorship of the domain name registration in the registry is\ncurrently set to expire. This date does not necessarily reflect the expiration\ndate of the domain name registrant's agreement with the sponsoring\nregistrar.  Users may consult the sponsoring registrar's Whois database to\nview the registrar's reported date of expiration for this registration.\n\nTERMS OF USE: You are not authorized to access or query our Whois\ndatabase through the use of electronic processes that are high-volume and\nautomated except as reasonably necessary to register domain names or\nmodify existing registrations; the Data in VeriSign Global Registry\nServices' (\"VeriSign\") Whois database is provided by VeriSign for\ninformation purposes only, and to assist persons in obtaining information\nabout or related to a domain name registration record. VeriSign does not\nguarantee its accuracy. By submitting a Whois query, you agree to abide\nby the following terms of use: You agree that you may use this Data only\nfor lawful purposes and that under no circumstances will you use this Data\nto: (1) allow, enable, or otherwise support the transmission of mass\nunsolicited, commercial advertising or solicitations via e-mail, telephone,\nor facsimile; or (2) enable high volume, automated, electronic processes\nthat apply to VeriSign (or its computer systems). The compilation,\nrepackaging, dissemination or other use of this Data is expressly\nprohibited without the prior written consent of VeriSign. You agree not to\nuse electronic processes that are automated and high-volume to access or\nquery the Whois database except as reasonably necessary to register\ndomain names or modify existing registrations. VeriSign reserves the right\nto restrict your access to the Whois database in its sole discretion to ensure\noperational stability.  VeriSign may restrict or terminate your access to the\nWhois database for failure to abide by these terms of use. VeriSign\nreserves the right to modify these terms at any time.\n\nThe Registry database contains ONLY .COM, .NET, .EDU domains and\nRegistrars.",
            'createdAt': '2016-11-30T19:38:52.505',
            'commentCount': 0,
            'likeCount': 1,
            'likedByCurrentUser': null,
            'creator': {
                'id': 7,
                'username': 'lipusal',
                'profilePictureUrl': 'http://localhost:8080/api/users/7/picture',
                'creatorUrl': 'http://localhost:8080/api/users/7'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/11/comments',
            'likesUrl': 'http://localhost:8080/api/threads/11/likes'
        }, {
            'id': 12,
            'title': 'Meta',
            'body': 'metaaaaaaaa',
            'createdAt': '2016-11-30T20:58:46.628',
            'commentCount': 0,
            'likeCount': 0,
            'likedByCurrentUser': null,
            'creator': {
                'id': 23,
                'username': 'pampero',
                'profilePictureUrl': 'http://localhost:8080/api/users/23/picture',
                'creatorUrl': 'http://localhost:8080/api/users/23'
            },
            'commentsUrl': 'http://localhost:8080/api/threads/12/comments',
            'likesUrl': 'http://localhost:8080/api/threads/12/likes'
        }];
    });
});
