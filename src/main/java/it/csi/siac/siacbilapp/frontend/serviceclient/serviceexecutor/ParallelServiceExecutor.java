/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.serviceclient.serviceexecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;


/**
 * Parallel service executor.
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/07/2015
 *
 */
public class ParallelServiceExecutor {
	
	private static final LogUtil LOG = new LogUtil(ParallelServiceExecutor.class);
	
	private Executor executor;
	private CompletionService<ServiceResponse> completionService;
	private int submitted = 0;
	private Map<Future<ServiceResponse>, String> nameMap;
	
	/**
	 * Post-construct hook.
	 */
	@PostConstruct
	public void postConstruct() {
		// FIXME per test
		executor = Executors.newFixedThreadPool(5);
		// TODO: estendere correttamente il completionService per gestire la nomenclatura dei task
		completionService = new ExecutorCompletionService<ServiceResponse>(executor);
		nameMap = new HashMap<Future<ServiceResponse>, String>();
	}
	
	/**
	 * Submits a task to the executor for later retieval.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param <SER> the service type
	 * @param invoker  the service invoker which takes care of the actual invocation in the new thread
	 * @param request  the request to invoke the service for
	 * @param service  the service to call by the invoker
	 * @param taskName the name of the task
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse, SER> void submitTask(final ServiceInvoker<REQ, RES, SER> invoker, final REQ request, final SER service, String taskName) {
		submitted++;
		Future<ServiceResponse> future = completionService.submit(new Callable<ServiceResponse>() {
			@Override
			public ServiceResponse call() throws Exception {
				return invoker.invoke(request, service);
			}
		});
		nameMap.put(future, taskName);
	}
	
	/**
	 * Submits a task to the executor for later retieval.
	 * <br/>
	 * Generates the task name via the keyAdapter.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param <SER> the service type
	 * @param invoker    the service invoker which takes care of the actual invocation in the new thread
	 * @param request    the request to invoke the service for
	 * @param service    the service to call by the invoker
	 * @param keyAdapter the adapter that generates the task name for the request
	 * @see #submitTask(ServiceInvoker, ServiceRequest, Object, String)
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse, SER> void submitTask(final ServiceInvoker<REQ, RES, SER> invoker, final REQ request, final SER service, KeyAdapter<REQ> keyAdapter) {
		submitTask(invoker, request, service, keyAdapter.computeKey(request));
	}
	
	/**
	 * Submits a task to the executor for later retieval.
	 * <br/>
	 * Generates the task name via the simple name of the request class.
	 * @param <REQ> the request type
	 * @param <RES> the response type
	 * @param <SER> the service type
	 * @param invoker  the service invoker which takes care of the actual invocation in the new thread
	 * @param request  the request to invoke the service for
	 * @param service  the service to call by the invoker
	 * @see #submitTask(ServiceInvoker, ServiceRequest, Object, String)
	 */
	public <REQ extends ServiceRequest, RES extends ServiceResponse, SER> void submitTask(final ServiceInvoker<REQ, RES, SER> invoker, final REQ request, final SER service) {
		submitTask(invoker, request, service, request.getClass().getSimpleName());
	}
	
	/**
	 * Gets the tasks submitted to the executor.
	 * 
	 * @return the tasks
	 * @throws WebServiceInvocationFailureException in case of exception in service invocation
	 */
	public Map<String, ServiceResponse> getTasks() throws WebServiceInvocationFailureException {
		final String methodName = "getTasks";
		Map<String, ServiceResponse> res = new HashMap<String, ServiceResponse>();
		
		while(submitted > 0) {
			try {
				Future<ServiceResponse> future = completionService.take();
				String str = nameMap.remove(future);
				ServiceResponse response = future.get();
				res.put(str, response);
			} catch (InterruptedException e) {
				LOG.warn(methodName, "InterruptedException in retrieving the future from the executor", e);
				throw new WebServiceInvocationFailureException(e);
			} catch (ExecutionException e) {
				LOG.warn(methodName, "ExecutionException in retrieving the future from the executor", e);
				throw new WebServiceInvocationFailureException(e);
			}
			
			submitted--;
		}
		
		return res;
	}
	
}
