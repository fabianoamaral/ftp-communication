package br.com.ftpcommunication;

import java.io.IOException;

import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.transport.IgnoreHostKeyVerification;

public class SshConnectionFactory {
	
	public static SshConnectionHolder createSshConnection(String server, String user, String password) throws IOException {
        SshClient client = new SshClient();
        client.connect(server, new IgnoreHostKeyVerification());
        PasswordAuthenticationClient auth = new PasswordAuthenticationClient();
		auth.setUsername(user);
		auth.setPassword(password);
		int status = client.authenticate(auth);
		if (status == AuthenticationProtocolState.COMPLETE){
            SshConnectionHolder sshHolder = new SshConnectionHolder(client);
            return sshHolder;
        }
		throw new IllegalStateException("Cannot authenticate - status: " + status);
    }

}
