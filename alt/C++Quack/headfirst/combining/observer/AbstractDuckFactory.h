#ifndef HEADFIRST_COMBINING_OBSERVER_ABSTRACT_DUCK_FACTORY_H
#define HEADFIRST_COMBINING_OBSERVER_ABSTRACT_DUCK_FACTORY_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "headfirst/combining/observer/Quackable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class AbstractDuckFactory
{
public:
	virtual Quackable createMallardDuck()=0;

	virtual Quackable createRedheadDuck()=0;

	virtual Quackable createDuckCall()=0;

	virtual Quackable createRubberDuck()=0;

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
