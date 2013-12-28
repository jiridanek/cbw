library math_component;

import 'package:angular/angular.dart' as ng;
import 'dart:html' as dom;
import 'package:js/js.dart' as js;

@ng.NgController (
    selector: '[math]',
    //templateUrl: 'packages/angular_dart_demo/component/view_recipe_component.html',
    map: const {
      'math': '=>math',
      'text': '@text'
    }
)
class MathComponent implements ng.NgShadowRootAware, ng.NgAttachAware {
  //ng.Scope _scope;
//  dom.Element _element;
//  ng.NgModel _ngModel;
//  ng.NodeAttrs _attrs;
  dom.Element _root;

  var context = js.context;

    var math;
    var text;

  //MathComponent(this._scope) {//, this._element, this._ngModel, this._attrs) {

  //}

  void onShadowRoot(dom.ShadowRoot shadowRoot) {
    //_root = shadowRoot;
    //_process();
  }

  MathComponent(dom.Element this._root) {
  }

  @override
  attach() {
    _process();
  }



  void _process() {
    var p = new dom.SpanElement();
    if (math is String && text != null) {
      p.text = math;
    } else {
      p.text = "\$${math}\$";
    }
    _root.append(p);
    context.MathJax.Hub.Queue(js.array(["Typeset", context.MathJax.Hub, p]));
  }

//  void _process(String math) {
//    var e = new dom.ParagraphElement()
      //..type = 'math/tex'
//      ..innerHtml = math // FIXME: the string is sanitized, math may get broken,
//    ;                    // not sanitizing at all means creating XSS opportunity
    //_element.firstChild.replaceWith(e);
//    dom.querySelector('#test').append(e);
    //print(js.context['MathJax']);
    //var v = js.context['MathJax']['Hub'].callMethod('Queue', [ ["Typeset", (js.context['MathJax']['Hub'])] ]);
    //print(v);

    //js.Proxy context = js.context;
    //context.MathJax.Hub.Queue(js.array(["Typeset", context.MathJax.Hub]));
//  }
}