package com.nzb.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.nzb.loadbalance.NodeInfo;

public class RmiUtil {

	public void startRmiServer(String host, String port, String id) {
		try {
			SoaRmi soaRmi = new SoaRmiImpl();
			LocateRegistry.createRegistry(Integer.valueOf(port));

			Naming.bind("rmi://" + host + ":" + port + "/" + id, soaRmi);
			System.out.println("rmi server start !!!");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public SoaRmi startRmiClient(NodeInfo nodeInfo, String id) {
		String host = nodeInfo.getHost();
		String port = nodeInfo.getPort();

		try {
			return (SoaRmi) Naming.lookup("rmi://" + host + ":" + port + "/" + id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
