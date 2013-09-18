#ifndef HEADFIRST_COMBINING_OBSERVER_OBSERVABLE_H
#define HEADFIRST_COMBINING_OBSERVER_OBSERVABLE_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/util/ArrayList.h"
#include "java/util/Iterator.h"
#include "headfirst/combining/observer/QuackObservable.h"
#include "headfirst/combining/observer/Observer.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class Observable : public QuackObservable
{
protected:
	java::util::ArrayList observers;

	QuackObservable duck;

public:
	Observable(QuackObservable duck);

	void registerObserver(Observer observer);

	void notifyObservers();

	java::util::Iterator getObservers();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
