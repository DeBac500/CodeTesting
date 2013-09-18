#ifndef HEADFIRST_COMBINING_OBSERVER_COUNTING_DUCK_FACTORY_H
#define HEADFIRST_COMBINING_OBSERVER_COUNTING_DUCK_FACTORY_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "headfirst/combining/observer/AbstractDuckFactory.h"
#include "headfirst/combining/observer/Quackable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class CountingDuckFactory : public AbstractDuckFactory
{
public:
	Quackable createMallardDuck();

	Quackable createRedheadDuck();

	Quackable createDuckCall();

	Quackable createRubberDuck();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
