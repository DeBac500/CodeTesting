#ifndef HEADFIRST_COMBINING_OBSERVER_QUACKOLOGIST_H
#define HEADFIRST_COMBINING_OBSERVER_QUACKOLOGIST_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"
#include "headfirst/combining/observer/Observer.h"
#include "headfirst/combining/observer/QuackObservable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class Quackologist : public Observer
{
public:
	void update(QuackObservable duck);

	java::lang::String toString();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
