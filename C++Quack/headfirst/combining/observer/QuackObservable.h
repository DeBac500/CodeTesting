#ifndef HEADFIRST_COMBINING_OBSERVER_QUACK_OBSERVABLE_H
#define HEADFIRST_COMBINING_OBSERVER_QUACK_OBSERVABLE_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "headfirst/combining/observer/Observer.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class QuackObservable
{
public:
	virtual void registerObserver(Observer observer)=0;

	virtual void notifyObservers()=0;

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
