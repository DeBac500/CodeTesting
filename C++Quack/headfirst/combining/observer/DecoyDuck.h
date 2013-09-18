#ifndef HEADFIRST_COMBINING_OBSERVER_DECOY_DUCK_H
#define HEADFIRST_COMBINING_OBSERVER_DECOY_DUCK_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"
#include "headfirst/combining/observer/Quackable.h"
#include "headfirst/combining/observer/Observable.h"
#include "headfirst/combining/observer/Observer.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class DecoyDuck : public Quackable
{
protected:
	Observable observable;

public:
	DecoyDuck();

	void quack();

	void registerObserver(Observer observer);

	void notifyObservers();

	java::lang::String toString();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
