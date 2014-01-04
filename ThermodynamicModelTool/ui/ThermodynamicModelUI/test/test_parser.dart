import 'package:unittest/unittest.dart';

import 'package:ThermodynamicModelUI/parser.dart';

main() {
  group('Parser', () {
    test('should accept simple equation', () {
      //expect();
      print(reaction.parse("A =>[MA(k1)] B"));
    });
  });
}