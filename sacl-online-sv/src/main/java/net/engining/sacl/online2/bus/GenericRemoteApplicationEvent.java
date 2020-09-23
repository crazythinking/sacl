package net.engining.sacl.online2.bus;

import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * {@link RemoteApplicationEvent} for Supporting Generic
 *
 */
public class GenericRemoteApplicationEvent<T> extends RemoteApplicationEvent {

	private T target;

	public GenericRemoteApplicationEvent() {
	}

	public GenericRemoteApplicationEvent(Object source, T target, String originService, String destinationService) {
		super(source, originService, destinationService);
		this.target = target;
	}

	public T getTarget() {
		return target;
	}

	public void setTarget(T target) {
		this.target = target;
	}
}
