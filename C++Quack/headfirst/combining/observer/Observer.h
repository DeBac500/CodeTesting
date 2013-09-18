#ifndef HEADFIRST_COMBINING_OBSERVER_OBSERVER_H
#define HEADFIRST_COMBINING_OBSERVER_OBSERVER_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "headfirst/combining/observer/QuackObservable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class Observer
{
public:
	virtual void update(QuackObservable duck)=0;

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
