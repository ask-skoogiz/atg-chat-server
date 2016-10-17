(function () {
    'use strict';

    angular
        .module('app')
        .controller('Register.IndexController', Controller);

    function Controller($location, AuthenticationService) {
        var vm = this;

        vm.register = register;

        initController();

        function initController() {
        }

        function register() {
          vm.loading = true;
          AuthenticationService.Register(vm.username, vm.password, function (result) {
              if (result.success === true) {
                  $location.path('/');
              } else {
                  vm.error = result.msg;
                  vm.loading = false;
              }
          });
        };
    }
})();
