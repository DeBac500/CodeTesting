#ifndef HEADFIRST_COMBINING_OBSERVER_GOOSE_H
#define HEADFIRST_COMBINING_OBSERVER_GOOSE_H

#include <string>
#include <vector>
#include <list>
#include <iostream>
#include <assert.h>

#include "java/lang/String.h"

namespace headfirst
{
namespace combining
{
namespace observer
{
class Goose
{
public:
	void honk();

	java::lang::String toString();

};

}  // namespace observer
}  // namespace combining
}  // namespace headfirst
#endif
