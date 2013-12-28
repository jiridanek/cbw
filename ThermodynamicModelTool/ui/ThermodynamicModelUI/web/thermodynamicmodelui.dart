import 'package:angular/angular.dart' as ng;
//import 'package:angular/routing/module.dart' as ng;
import 'package:logging/logging.dart' as logging;

import 'dart:html' as dom;
import 'package:perf_api/perf_api.dart';

//@MirrorsUsed(symbols: const ['pes', 'kocka'])
@MirrorsUsed(
targets: const [
                'angular.core',
                'angular.core.dom',
                'angular.core.parser',
                'angular.routing',
                'angular',
                'angular.filter',
                'angular.perf',
                'angular.directive',
                'expression=', //?
                '=', //?
                dom.NodeTreeSanitizer,
                Profiler,
                ng.NgRepeatDirective,
                'ng.NgRepeatDirective.expression',
                ThermodynamicModelUIRouteInitializer,
                ThermodynamicModelUIController,
                ViewConstantsComponent,
                MathComponent,
                Constant,
                Species
                ],
                metaTargets: const [
                                    ng.NgInjectableService,
                                    ng.NgComponent,
                                    ng.NgRepeatDirective,
                                    ng.NgDirective,
                                    ng.NgController,
                                    ng.NgFilter,
                                    ng.NgAttr,
                                    ng.NgBindDirective, //?
                                    ng.NgOneWay,
                                    ng.NgOneWayOneTime,
                                    ng.NgTwoWay,
                                    ng.NgCallback
                                    ],
                                    override: '*'
                                      )
import 'dart:mirrors' show MirrorsUsed;


import 'package:js/js.dart' as js;

import 'package:ThermodynamicModelUI/thermodynamic_model_ui.dart';
import 'package:ThermodynamicModelUI/routing/thermodynamic_model_ui_router.dart';
import 'package:ThermodynamicModelUI/mathjax/math_component.dart';
import 'package:ThermodynamicModelUI/component/view_constants_component.dart';


class MyAppModule extends ng.Module {
  MyAppModule() {
    type(Profiler, implementedBy: Profiler); // comment out to enable profiling
    type(ThermodynamicModelUIController);
    type(MathComponent);
    type(ViewConstantsComponent);
    type(ng.RouteInitializer, implementedBy: ThermodynamicModelUIRouteInitializer);
    factory(ng.NgRoutingUsePushState,
        (_) => new ng.NgRoutingUsePushState.value(false));
  }
}

main() {
  //logging.Logger.root.level = logging.Level.FINEST;
  //logging.Logger.root.onRecord.listen((logging.LogRecord r) { print(r.message); });

 // var context = js.context;



 // context.pes = "les";

  ng.ngBootstrap(module: new MyAppModule());

 // context.kocka = "pocka";

}