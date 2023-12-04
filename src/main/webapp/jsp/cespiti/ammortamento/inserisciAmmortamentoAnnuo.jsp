<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="#">
						<s:hidden name="ambito" id="ambito" />
						<h3 id="titolo"> Ammortamento annuo</h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset id="campiAmmortamentoAnnuo" class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="annoAmmortamentoAnnuo">Anno * </label>
										<div class="controls">
											<s:textfield id="annoAmmortamentoAnnuo" name="annoAmmortamentoAnnuo" maxlength="4" cssClass="span1 soloNumeri" />
										</div>
									</div>									
									<p class="margin-medium">
										<button id="pulsanteEffettuaScritture" type="button" class="btn btn-primary pull-right" >effettua scritture</button>
										<button id="pulsanteInserisciAnteprimaCespiti" type="button" class="btn btn-primary pull-right" >inserisci anteprima</button>
									</p>
								</fieldset>	
								<div id="risultatiRicercaAnteprimaAmmortamento" class="hide" >
									<h4>Elenco Scritture </h4>
									<fieldset id="fieldsetRisultatiRicercaAnteprimaAmmortamento" class="form-horizontal" class="hide">
										<h4><span id="id_num_result" class="num_result"></span></h4>
										<table class="table table-hover tab_left" id="tabellaAnteprima">
										    <thead>                                                                 
										        <tr>                                                                
													<th>Conto</th>
													<th>Descrizione</th>                                                   
													<th class="tab_Right">Numero Cespiti</th>
													<th class="tab_Right">Dare</th>                                 
													<th class="tab_Right">Avere</th>
													<th class="tab_Right">&nbsp;</th>                               
												</tr>                                                                
											</thead>                                                                 
											<tbody>                                                                  
											</tbody>                                                                 
										</table> 
									</fieldset>
								</div>
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn reset">annulla</button>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/ammortamento/inserisciAmmortamentoAnnuo.js"></script>
</body>
</html>