#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "QuackCounter.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
int QuackCounter::numberOfQuacks;

QuackCounter::QuackCounter(Quackable duck)
{
}

void QuackCounter::quack()
{
}

int QuackCounter::getQuacks()
{
	return 0;
}

void QuackCounter::registerObserver(Observer observer)
{
}

void QuackCounter::notifyObservers()
{
}

java::lang::String QuackCounter::toString()
{
	return 0;
}
}  // namespace observer
}  // namespace combining
}  // namespace headfirst
