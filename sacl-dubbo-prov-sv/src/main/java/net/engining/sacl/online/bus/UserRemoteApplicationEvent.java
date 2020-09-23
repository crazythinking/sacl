package net.engining.sacl.online.bus;

import net.engining.sacl.online.service.User;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * {@link User} {@link RemoteApplicationEvent}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 0.2.1
 */
public class UserRemoteApplicationEvent extends RemoteApplicationEvent {

	private User user;

	public UserRemoteApplicationEvent() {
	}

	public UserRemoteApplicationEvent(Object source, User user, String originService,
									  String destinationService) {
		super(source, originService, destinationService);
		this.user = user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
