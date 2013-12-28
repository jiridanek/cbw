library thermodynamic_model_ui_routing;

import 'package:angular/angular.dart' as ng;

class ThermodynamicModelUIRouteInitializer implements ng.RouteInitializer {
  init(ng.Router router, ng.ViewFactory view) {
    router.root
      ..addRoute(
          name: "constants",
          path: "constants",
          enter: view("view/view_constants.html"))
      ..addRoute(
          name: 'about',
          path: "about",
          enter: view("view/view_about.html"))
      ..addRoute(
          name: 'help',
          path: 'help',
          enter: view("view/view_help.html"))
      ..addRoute(
          name: 'view_default',
          defaultRoute: true,
          enter: view("view/view_welcome.html"))
    ;
  }
}