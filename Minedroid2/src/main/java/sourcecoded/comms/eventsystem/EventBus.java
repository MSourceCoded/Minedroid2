package sourcecoded.comms.eventsystem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventBus {

	public static class Registry {
		@SuppressWarnings("rawtypes")
		private static List<Class> handlers = new ArrayList<Class>();

	    @SuppressWarnings("rawtypes")
		public static void register(Class clazz) {
	        handlers.add(clazz);
	    }

	    @SuppressWarnings("rawtypes")
		public static List<Class> getHandlers() {
	        return handlers;
	    }
	}
	
	public static class Publisher {
		public static void raiseEvent(final IEvent event) {
	        new Thread() {
	            @Override
	            public void run() {
	                raise(event);
	            }
	        }.start();
	    }

	    @SuppressWarnings("rawtypes")
		private static void raise(final IEvent event) {
	        for (Class handler : EventBus.Registry.getHandlers()) {
	            Method[] methods = handler.getMethods();

	            for (int i = 0; i < methods.length; ++i) {
	                SourceCommsEvent eventHandler = methods[i].getAnnotation(SourceCommsEvent.class);
	                if (eventHandler != null) {
	                    Class[] methodParams = methods[i].getParameterTypes();
	                    
	                    if (methodParams.length < 1)
	                        continue;

	                    if (!event.getClass().getSimpleName()
	                            .equals(methodParams[0].getSimpleName()))
	                        continue;

	                    // defence from runtime exceptions:
	                    try {
	                        methods[i].invoke(handler.newInstance(), event);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	}
	
}
