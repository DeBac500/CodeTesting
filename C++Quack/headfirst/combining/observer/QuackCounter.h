#ifndef HEADFIRST_COMBINING_OBSERVER_QUACK_COUNTER_H
#define HEADFIRST_COMBINING_OBSERVER_QUACK_COUNTER_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"
#include "headfirst/combining/observer/Quackable.h"
#include "headfirst/combining/observer/Observer.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class QuackCounter : public Quackable
{
protected:
	static int numberOfQuacks;

	Quackable duck;

public:
	QuackCounter(Quackable duck);

	void quack();

	static int getQuacks();

	void registerObserver(Observer observer);

	void notifyObservers();

	java::lang::String toString();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
