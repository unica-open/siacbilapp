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
					<s:form id="formInserisciCespite" cssClass="form-horizontal" novalidate="novalidate" action="%{baseUrl}_salva">
						<s:hidden name="ambito" id="ambito" />
						<s:hidden name="uidMovimentoDettaglio" id="uidMovimentoDettaglio" />
						<s:hidden name="uidPrimaNota" id="uidPrimaNota" />
						<s:hidden name="importoMassimoCespite" />
						<h3 id="titolo"><s:property value="formTitle"/></h3>
						<s:include value="/jsp/include/messaggi.jsp" />
						<div class="step-content">
							<br/>
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="codice" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codice" name="cespite.codice" cssClass="span6" placeholder="codice" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizione" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textfield id="descrizione" name="cespite.descrizione" cssClass="span6" placeholder="descrizione" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="cespiteTipoBene">Tipo Bene *</label>
										<div class="controls">

											<select id="cespiteTipoBene" name="cespite.tipoBeneCespite.uid" class="span6" required>
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
											<span class="alRight">
												&nbsp;
											</span>
											<s:textfield id="contoPatrimonialeCespiteTipoBene" cssClass="span5" disabled="true" required="true"/>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label" for="cespiteClassificazioneGiuridica">Classificazione giuridica *</label>
										<div class="controls">
											<s:select id="cespiteTipoBene" cssClass="span6" list="listaClassificazioneGiuridicaCespite" name="cespite.classificazioneGiuridicaCespite"
														required="true" headerKey="" headerValue="" listValue="%{codice + ' - '+ descrizione}" />
											<span id="contoPatrimonaleTipoBene"></span>
										</div>
									</div>
									<div class="control-group">
										<label for="beniCulturali" class="control-label">Soggetto beni culturali </label>
										<div class="controls">
											<s:checkbox id="beniCulturali" name="cespite.flagSoggettoTutelaBeniCulturali" />
										</div>
									</div>
									<div class="control-group">
										<label for="numeroInventario" class="control-label"><abbr title="Numero">Num.</abbr> inventario</label>
										<div class="controls">
											<s:textfield id="numeroInventario" name="cespite.numeroInventario" cssClass="lbTextSmall span2" required="true" disabled="true" maxlength="200"/>
											<span class="alRight">
												<label for="dataIngressoInventario" class="radio inline">Data ingresso in inventario *</label>
											</span>
											<s:textfield id="dataIngressoInventario" name="cespite.dataAccessoInventario" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</div>
									</div>
									<div class="control-group">
										<label for="copiaNumeroInventario" class="control-label">Copia i dati da <abbr title="Numero">N.</abbr> inventario</label>
										<div class="controls">
											<s:textfield id="copiaNumeroInventario" name="copiaNumeroInventario" cssClass="lbTextSmall span2" maxlength="200"/>
											<button type="button" class="btn btn-primary pull-right" data-submit-url="<s:property value="baseUrl" />_copia.do">Copia</button>
										</div>
									</div>

									<div class="control-group">
										<label class="control-label" for="valoreIniziale">Valore Iniziale *</label>
										<div class="controls">
											<s:textfield id="valoreIniziale" cssClass="lbTextSmall span2 soloNumeri decimale" name="cespite.valoreIniziale" placeholder="valore iniziale" required="required" />
										</div>
									</div>
									<div class="control-group">
										<span class="control-label">Attivo *</span>
										<div class="controls">
											<label class="radio">
												<input type="radio" name="cespite.flagStatoBene" value="true" checked="checked" disabled="disabled"/>
												S&igrave;
											</label>
											<label class="radio">
												<input type="radio" name="cespite.flagStatoBene" value="false" disabled="disabled"/>
												No
											</label>
											<s:hidden name="cespite.flagStatoBene" value="true"/>
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneStato" class="control-label">Descrizione stato *</label>
										<div class="controls">
											<s:textfield id="descrizioneStato" name="cespite.descrizioneStato" cssClass="span6" value="Attivo" required="true"  maxlength="500"/>
										</div>
									</div>
									<div class="control-group">
										<label for="ubicazione" class="control-label">Ubicazione</label>
										<div class="controls">
											<s:textarea id="ubicazione" name="cespite.ubicazione" class="span6" rows="3" cols="5"></s:textarea>
										</div>
									</div>
									<div class="control-group">
										<label for="note" class="control-label">Note</label>
										<div class="controls">
											<s:textarea id="note" name="cespite.note" class="span6" rows="5" cols="5"></s:textarea>
										</div>
									</div>
								</fieldset>
							</div>
						</div>
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<%--<button type="button" class="btn reset">annulla</button> --%>
							<button id="pulsanteAnnulla" name="pulsanteAnnulla" type="button" class="btn" >annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/cespite/inserisciCespite.js"></script>
</body>
</html>