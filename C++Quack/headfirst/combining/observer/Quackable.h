#ifndef HEADFIRST_COMBINING_OBSERVER_QUACKABLE_H
#define HEADFIRST_COMBINING_OBSERVER_QUACKABLE_H

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
class Quackable : public QuackObservable
{
public:
	virtual void quack()=0;

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
