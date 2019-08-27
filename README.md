# discretizer4j
[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)
[![Build Status](https://travis-ci.org/viadee/javaAnchorExplainer.svg?branch=master)](https://travis-ci.org/viadee/discretizer4j)
This project provides a Java implementation of several discretization algorithms (aka binning).

This is often a useful step in order to cope with overfitting in machine learning models or overly specific explanations from XAI algorithms such as [Anchors](https://github.com/viadee/javaAnchorExplainer), when working with numerical data.

We concentrate on univariate algorithms, both supervised and unsupervised, to keep things simple and away from decision tree algorithms.
We chose the Java language to achieve a reasonable performance, to easily integrate with AnchorsJ (and because we did not find any other  suitable open source java package).

Current implementations:
* Unsupervised: 
    * Equal Frequency in ``PercentileMedianDiscretizer``
    * [Equal Size](http://users.monash.edu/~webb/Files/YangWebb03b.pdf) in ``EqualSizeDiscretizer``
    * [Proportional k-Interval Discretizer](http://users.monash.edu/~webb/Files/YangWebb03b.pdf) in ``EqualSizeDiscretizer``
    * Manual Discretization in ``ManualDiscretizer``
    * Random Discretization in ``RandomDiscretizer``
* Supervised: 
    * [FUSINTER Discretizer](https://www.researchgate.net/publication/220354451_FUSINTER_A_Method_for_Discretization_of_Continuous_Attributes) in ``FUSINTERDiscretizer``
    * [Minimum Description Length Principle Discretizer](https://www.ijcai.org/Proceedings/93-2/Papers/022.pdf) in ``MDLPDiscretizer``
    * [Ameva Discretizer](https://sci2s.ugr.es/keel/pdf/algorithm/articulo/2009-Gonzalez-Abril-ESWA.pdf) in ``AmevaDiscretizer``
## Getting Started


### Prerequisites and Installation

In order to use the core project, no installation other than Java (version 8+) is are required. The intended way of using the algorithms is to use them as a maven depencency. Our maven coordinates are as follows:

```xml
  <dependency>
    <groupId>de.viadee</groupId>
    <artifactId>discretizer4j</artifactId>
    <version>1.0.0</version>    
  </dependency>
```
    
There are no transitive dependencies.

### Using the Algorithm

To discretize a continuous feature, one has to create a Discretizer (extending the ``AbstractDiscretizer``). The Discretizer then has to be fitted.
This may be built as follows: 

```Java
Discretizer discretizer = new Discretizer();
discretizer.fit(values, labels);
```
The fitted discretizer can then be used to get all ``DiscretizerTransitions``, that have been fitted by the algorithm. 
Or values can be applied to the discretizer, the apply function returns the discretized labels.

```Java
discretizer.getTransitions();
// returns:
// DiscretizationTransition From ]1, 14.5) to class 0.0
// DiscretizationTransition From [14.5, 19.5) to class 1.0
// DiscretizationTransition From [19.5, 22.5) to class 2.0
// DiscretizationTransition From [22.5, 36.5) to class 3.0
// DiscretizationTransition From [36.5, 40[ to class 4.0

discretizer.apply(new Double[]{1.5, 17.0, 10.0})
// returns:
// Double[0.0, 1.0, 0.0]
```

The fitting creates ``DiscretizerTransitions``. These consist of a discretizedLabel (Double) and a discretizedOrigin. 
The Origin is either a unique value, if the ``UniqueValueDiscretizer`` was used, or a combination of a minValue and maxValue, which determine the Interval limits of the Transition. 

### Tutorials and Examples

Small examples for all implemented discretizers can be found in the unit tests. 

To see these discretizers in a more complex project, please refer to the [XAI Examples](https://github.com/viadee/xai_examples). Here discretization was used in the context of explainable artificial intelligence. 

# Collaboration

The project is operated and further developed by the viadee Consulting AG in Münster, Westphalia. Results from theses at the WWU Münster and the FH Münster have been incorporated. Contact person is Dr. Frank Köhne from viadee.
* Implementation of additional Discretizers ar planned.
* Community contributions to the project are welcome: Please open Github-Issues with suggestions (or PR), which we can then edit in the team.

## Authors
* **Marvin Gronhorst** - [Marvin Gronhorst](https://github.com/MarvinGronhorst)
* **Tobias Goerke** - [Tobias Goerke](https://github.com/TobiasGoerke)
* **Colin Juers** - [Colin Juers](https://github.com/cjuers)
* **Dr. Frank Köhne** - [Dr. Frank Köhne](https://github.com/fkoehne)
 

## License

BSD 3-Clause License

## Acknowledgments

[Garcia et al.](https://sci2s.ugr.es/sites/default/files/files/ComplementaryMaterial/discretization/2013-Garcia-IEEETKDE.pdf) for the extensive research of discretization techniques. 
