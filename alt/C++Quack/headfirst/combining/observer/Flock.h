#ifndef HEADFIRST_COMBINING_OBSERVER_FLOCK_H
#define HEADFIRST_COMBINING_OBSERVER_FLOCK_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"
#include "java/util/ArrayList.h"
#include "headfirst/combining/observer/Quackable.h"
#include "headfirst/combining/observer/Observer.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class Flock : public Quackable
{
protected:
	java::util::ArrayList ducks;


public:
	Flock();
	void add(Quackable duck);

	void quack();

	void registerObserver(Observer observer);

	void notifyObservers();

	java::lang::String toString();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
