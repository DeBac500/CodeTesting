#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "Observable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{

Observable::Observable(QuackObservable duck)
:observers(new ArrayListnull())
{
}

void Observable::registerObserver(Observer observer)
{
}

void Observable::notifyObservers()
{
}

java::util::Iterator Observable::getObservers()
{
	return 0;
}
}  // namespace observer
}  // namespace combining
}  // namespace headfirst
