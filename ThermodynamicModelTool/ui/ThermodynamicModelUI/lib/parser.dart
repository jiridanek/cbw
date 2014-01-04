/**
 *
 *  2A =>[MA(k1)] B
 *  A =>[MA(k1)] _
 *  A <=>[MA(k1)][k2] B
 *  A + 3B <=>[MA($k_1$)][MYLAW($k_2$, B!$\tau_1$)] C + D
 *
 *  The spaces (and absences of spaces) are significant and required.
 *  In mhchem the content of [] is assumed to be math mode. This is not true here.
 *  MA stands for mass action kinetics
 *  names of species and rate laws may not begin with a number
 *  name of a species followed by ! and by a constant that signifies time delay
 *
 *  reserved names: MA, _
 *  forbidden characters: identifiers cannot contain square braces and slash: [, ], /
 *
 * The grammar for specifying equations is inspired by LaTeX package mhchem
 * and by BIOCHAM syntax.
 */

/*
 * Considered using antlr or PEG.js, decided that it is not good idea since 1) learning, 2) error reporting
 * By setting the limitations specified above parsing this is completely trivial.
 */

import 'dart:core';

import 'package:petitparser/petitparser.dart';

Parser id = (letter() & (letter() | digit() | char('\$') | char('_') | char('{') | char('}') | char('^')).star()).flatten();
Parser number = digit().plus().flatten().trim().map(int.parse);
Parser species = (number.optional() & id);
Parser reactants = species & ( string(' + ') & species ).star();
Parser params = id & ( string(', ') & id).star();
Parser rate = char('[') & id & char('(') & params & string(')]');
Parser arrowrates = string('<=>') | string('<=') | string('=>') & rate & rate.optional();
Parser reaction = reactants & char(' ') & arrowrates & char(' ') & reactants;

//
//class Reaction {}
//
//class Parser {
//  String s;
//  Parser(String this.s);
//  Reaction parse() {
//    return reaction(s);
//  }
//  Reaction reaction(String s) {
//    var left = species();
//    var middle = arrowrate();
//    var right = species();
//    return new Reaction(left, middle, right);
//  }
//  List<Specie> species() {
//    var l = new List<String>();
//    var next = specie();
//    l.add(next);
//    while(true) {
//      try {
//        next = specie();
//        expect('+');
//        l.add(next);
//      } catch (ParseFailed) {
//        return l;
//      }
//    }
//  }
//  Specie specie() {
//    var t = token();
//    var n = number();
//    var i = identifier();
//    return new Specie(n,i);
//  }
//  arrowrate() {
//    var t = token();
//    var type = arrow();
//    var lrrate = arate();
//    var rlate;
//    try {
//      rlrate = arate();
//    }
//    if (check('<=>')) {
//      type="<=>";
//    } else if (check('=>')) {
//      type="=>";
//    } else if (check('<=')) {
//      type = "<=";
//    } else {
//      throw new ParseFailed("Expected either <=>, <= or =>");
//    }
//  }
//}
//
//start = equation
//
//equation = species space arrowrate space species
//species = specie | specie space species
//specie = number identifier | identifier
//
//arrowrate = arrow rate
//arrow = => | <= | <=>
//rate = arate | arate arate
//arate = [identifier( params )]
//params = param | param, space params