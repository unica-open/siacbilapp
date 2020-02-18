<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:form method="post" action="effettuaRicercaVincolo">
						<h3>Ricerca Vincoli</h3>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<div class="fieldset">
							<div class="fieldset-group">
								<div class="fieldset-heading">
									<h4>Dati di ricerca</h4>
								</div>
								<div class="fieldset-body">
									<fieldset class="form-horizontal">
										<div class="control-group">
											<span class="control-label">Bilancio</span>
											<div class="controls">
												<label class="radio">
													<input type="radio" name="vincolo.tipoVincoloCapitoli" value="PREVISIONE" <s:if test='%{vincolo.tipoVincoloCapitoli.name() eq "PREVISIONE"}'>checked="checked"</s:if>> Previsione
												</label>
												<label class="radio">
													<input type="radio" name="vincolo.tipoVincoloCapitoli" value="GESTIONE" <s:if test='%{vincolo.tipoVincoloCapitoli.name() eq "GESTIONE"}'>checked="checked"</s:if>> Gestione
												</label>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label">Capitolo</label>
											<div class="controls">
												<label class="radio inline">
													<input type="radio" name="tipoCapitolo" value="ENTRATA" <s:if test='%{tipoCapitolo eq "ENTRATA"}'>checked="checked"</s:if>> Entrata
												</label>
												<label class="radio inline">
													<input type="radio" name="tipoCapitolo" value="USCITA" <s:if test='%{tipoCapitolo eq "USCITA"}'>checked="checked"</s:if>> Spesa
												</label>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="annoCapitolo">Anno</label>
												<div class="controls">
													<s:textfield id="annoCapitolo" name="annoEsercizioInt" cssClass="lbTextSmall span2" maxlength="4" disabled="true" />
													<s:hidden name="capitolo.annoCapitolo" value="%{annoEsercizioInt}" data-maintain="" />
												<span class="al">
													<label class="radio inline" for="numeroCapitolo">Capitolo</label>
												</span>
												<s:textfield id="numeroCapitolo" name="capitolo.numeroCapitolo" cssClass="lbTextSmall span2" maxlength="9" placeholder="%{'capitolo'}" />
												<span class="al">
													<label class="radio inline" for="numeroArticolo">Articolo</label>
												</span>
												<s:textfield id="numeroArticolo" name="capitolo.numeroArticolo" cssClass="lbTextSmall span2" maxlength="9" placeholder="articolo" />
												<s:if test="%{gestioneUEB}">
													<span class="al">
														<label class="radio inline" for="numeroUEB">UEB</label>
													</span>
													<s:textfield id="numeroUEB" name="capitolo.numeroUEB" cssClass="lbTextSmall span2" maxlength="9" placeholder="ueb" />
												</s:if>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="codiceVincolo">Codice vincolo</label>
											<div class="controls">
												<s:textfield id="codiceVincolo" cssClass="lbTextSmall span2" name="vincolo.codice" maxlength="200" placeholder="codice" />
											</div>
										</div>
										<div class="control-group">
											<span class="al">
												<label for="descrizioneVincolo" class="control-label">Descrizione</label>
											</span>
											<div class="controls">
												<s:textarea id="descrizioneVincolo" rows="1" cols="15" cssClass="span10" name="vincolo.descrizione" maxlegth="500"></s:textarea>
											</div>
										</div>
										<div class="control-group">
											<label class="control-label" for="trasferimentiVincolati">Trasferimenti vincolati</label>
											<div class="controls">
												<label class="radio inline">
													<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="false" <s:if test="%{vincolo.flagTrasferimentiVincolati == false}">checked="checked"</s:if>> No
												</label>
												<label class="radio inline">
													<input type="radio" name="vincolo.flagTrasferimentiVincolati" value="true" <s:if test="%{vincolo.flagTrasferimentiVincolati == true}">checked="checked"</s:if>> S&igrave;
												</label>
											</div>
										</div>
										<div class="control-group">
											<span class="al">
												<label for="genereVincolo" class="control-label">Tipo vincolo </label>
											</span>
											<div class="controls">
												<s:select list="listaGenereVincolo" id="genereVincolo" name="vincolo.genereVincolo.uid" cssClass="span10" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
													headerKey="" headerValue="" />
											</div>
										</div>
									</fieldset>
									<p>
										<s:include value="/jsp/include/indietro.jsp" />
										<button type="button" class="btn btn-secondary reset">annulla</button>
										<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
									</p>
								</div>
							</div>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>