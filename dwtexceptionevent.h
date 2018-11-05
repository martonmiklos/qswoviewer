#ifndef DWTEXCEPTIONEVENT_H
#define DWTEXCEPTIONEVENT_H

#include "itmevent.h"

class DWTExceptionEvent : public ITMEvent
{
public:
    DWTExceptionEvent(int exceptionNumber, byte function);
};

#endif // DWTEXCEPTIONEVENT_H
