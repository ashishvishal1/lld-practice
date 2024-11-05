# Observer Pattern
- 3 Entities are available in this pattern


1. Eventmanagaer: Contains the map of event and subscriber, also have loic to add/remove/notify subscriber.
2. Event: An Object on which any changes will be notified to the listener by eventManager.
2. Listener: An interface which will be implemented by multiple type of listener who will subscribe to the event.