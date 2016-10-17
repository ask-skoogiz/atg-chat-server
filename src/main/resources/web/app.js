(function () {
  'use strict';

  var app = angular
  .module('app', ['ui.router', 'ngMessages', 'ngStorage'])
  .config(config)
  .run(run);

  app.controller("NavController", function($rootScope, $location, $localStorage) {

    var vm = this;

    initController();

    function initController() {
      loggedIn();
    };

    $rootScope.$on('$locationChangeStart', function (event, next, current) {
      loggedIn();
    });

    function loggedIn() {
      var noShow = ['/login', '/register'];
      vm.loggedIn = noShow.indexOf($location.path()) === -1;
    };
  });

  function config($stateProvider, $urlRouterProvider) {
    // default route
    $urlRouterProvider.otherwise("/");

    // app routes
    $stateProvider
    .state('home', {
      url: '/',
      templateUrl: 'home/index.view.html',
      controller: 'Home.IndexController',
      controllerAs: 'vm'
    })
    .state('register', {
      url: '/register',
      templateUrl: 'register/index.view.html',
      controller: 'Register.IndexController',
      controllerAs: 'vm'
    })
    .state('login', {
      url: '/login',
      templateUrl: 'login/index.view.html',
      controller: 'Login.IndexController',
      controllerAs: 'vm'
    });
  }

  function run($rootScope, $http, $location, $localStorage) {
    // keep user logged in after page refresh
    if ($localStorage.currentUser) {
      $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    // redirect to login page if not logged in and trying to access a restricted page
    $rootScope.$on('$locationChangeStart', function (event, next, current) {
      var publicPages = ['/login', '/register'];
      var restrictedPage = publicPages.indexOf($location.path()) === -1;
      if (restrictedPage && !$localStorage.currentUser) {
        $location.path('/login');
      }
    });
  }
})();
