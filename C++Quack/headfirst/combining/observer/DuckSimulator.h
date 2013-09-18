#ifndef HEADFIRST_COMBINING_OBSERVER_DUCK_SIMULATOR_H
#define HEADFIRST_COMBINING_OBSERVER_DUCK_SIMULATOR_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"
#include "headfirst/combining/observer/AbstractDuckFactory.h"
#include "headfirst/combining/observer/Quackable.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class DuckSimulator
{
protected:
	void simulate(AbstractDuckFactory duckFactory);

	void simulate(Quackable duck);

public:
	static void main(java::lang::String args[]);

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
