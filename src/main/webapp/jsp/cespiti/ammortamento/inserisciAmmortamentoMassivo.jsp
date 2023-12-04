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

	<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form id="formInserisciTipoBene" cssClass="form-horizontal" novalidate="novalidate" action="ricercaTipoBene_effettuaRicerca">
						<s:hidden name="ambito" id="ambito" />
						<h3 id="titolo"> Ammortamento massivo</h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset id="campiRicercaCespite" class="form-horizontal">
									<div class="control-group">
										<label class="control-label" for="ultimoAnnoAmmortamento">Ultimo anno di ammortamento * </label>
										<div class="controls">
											<s:textfield id="ultimoAnnoAmmortamento" name="ultimoAnnoAmmortamento" maxlength="4" cssClass="span1 soloNumeri" />
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="cespiteTipoBene">Tipo Bene </label>
										<div class="controls">
										
											<select id="cespiteTipoBene" name="cespite.tipoBeneCespite.uid" class="span6">
												<option value="0"></option>
												<s:iterator value="listaTipoBeneCespite" var="ltbc">
													<option value="<s:property value="%{#ltbc.uid}"/>"
														<s:if test="%{#ltbc.contoPatrimoniale != null}">
															data-conto-patrimoniale="<s:property value="%{#ltbc.contoPatrimoniale.codice + ' - ' + #ltbc.contoPatrimoniale.descrizione}"/>"
														</s:if>
														<s:if test="%{#ltbc.uid == cespite.tipoBeneCespite.uid}">
															selected
														</s:if>
														>
														<s:property value="%{#ltbc.codice + ' - ' + #ltbc.descrizione}"/>
													</option>
												</s:iterator>
											</select>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="codiceConto">Conto patrimoniale</label>
										<div class="controls">
											<s:textfield id="codiceContoPatrimoniale" name="cespite.tipoBeneCespite.contoPatrimoniale.codice" cssClass="span6" maxlength="200" />
											<span id="descrizioneContoPatrimonale"></span>
											<span class="radio guidata">
												<button type="button" data-selector-conto="<s:property value="contoPatrimonialeTipoBeneSelector"/>" data-selector-classe="<s:property value="contoPatrimonialeTipoBeneSelector.codiceClassePiano"/>" class="btn btn-primary" id="pulsanteCompilazioneGuidataContoPatrimoniale">compilazione guidata</button>
											</span>
										</div>
									</div>
									<p class="margin-medium">
										<button id="pulsanteRicercaCespiti" type="button" class="btn btn-primary pull-right" >cerca schede cespite da elaborare</button>
									</p>
								</fieldset>	
								<div id="risultatiRicercaCespitiDaAmmortare" class="hide" >
									<fieldset class="form-horizontal">
										<h4><span id="id_num_result" class="num_result"></span></h4>
										<table class="table table-hover tab_left dataTable" id="tabellaRisultatiRicercaCespite">
											<thead>
												<tr>
													<th class="span1">
														<input type="checkbox" class="tooltip-test" data-original-title="Seleziona tutti" id="selezionaTuttiCespiti"/>
													</th>
													<th>Codice</th>
													<th>Descrizione</th>
													<th>Tipo Bene</th>
													<th>Inventario</th>
													<th>Data accesso inventario</th>
													<th>Valore del bene</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</fieldset>
									<s:if test="%{abilitaPulsantiAmmortamento}">
										<p class="margin-medium">
											<button  id="calcolaAmmortamentoSuSelezionati" type="button" class="btn btn-secondary pull-right">calcola selezionati</button>
										</p>
										<div class="clear"></div>
										<p class="margin-medium">
											<button id="calcolaAmmortamentoSuTutti" type="button" class="btn btn-secondary pull-right">calcola piani di ammortamento mancanti</button>	
										</p>
									</s:if>
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
	<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaConto.jsp" />
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/conto.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/cespiti/ammortamento/inserisciAmmortamentoMassivo.js"></script>
</body>
</html>