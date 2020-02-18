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

	<%-- Pagina JSP vera e propria --%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:form cssClass="form-horizontal" action="aggiornaGruppoAttivitaIva" id="formAggiornamentoGruppoAttivitaIva" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden name="uidGruppoAttivitaIva" data-maintain="" />
						<h3>Aggiorna gruppo attivit&agrave; iva</h3>
						<div class="step-content">
							<div class="step-pane active">
								<h4>Dati generali</h4>
								<fieldset class="form-horizontal">
									<div class="control-group">
										<label for="codiceGruppoAttivitaIva" class="control-label">Codice *</label>
										<div class="controls">
											<s:textfield id="codiceGruppoAttivitaIva" name="gruppoAttivitaIva.codice" cssClass="lbTextSnall span2" disabled="true" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="descrizioneGruppoAttivitaIva" class="control-label">Descrizione *</label>
										<div class="controls">
											<s:textarea id="descrizioneGruppoAttivitaIva" name="gruppoAttivitaIva.descrizione" cssClass="input-medium span9" cols="15" rows="1" disabled="true" data-maintain=""></s:textarea>
										</div>
									</div>
									<div class="control-group">
										<label for="tipoAttivita" class="control-label">Tipo attivit&agrave;</label>
										<div class="controls">
											<s:select list="listaTipoAttivita" id="tipoAttivita" name="tipoAttivita" cssClass="lbTextSmall span6" headerKey="" headerValue="" disabled="true" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="listaAttivitaIva" class="control-label">Attivit&agrave; *</label>
										<div class="controls">
											<s:select list="listaAttivitaIva" multiple="true" id="listaAttivitaIva" cssClass="span10 chosen-select"
												name="gruppoAttivitaIva.listaAttivitaIva.uid" listKey="uid" listValue="%{codice + ' - ' + descrizione}"
												data-placeholder="Seleziona l'attività" value="%{gruppoAttivitaIva.listaAttivitaIva.{uid}}" required="true" />
										</div>
									</div>
									<h4>Dati per anno di bilancio</h4>
									<div class="control-group">
										<label for="annoBilancio" class="control-label">Anno esercizio</label>
										<div class="controls">
											<s:textfield id="annoBilancio" name="annoEsercizio" cssClass="lbTextSmall span1" disabled="true" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="tipoChiusura" class="control-label">Tipo chiusura *</label>
										<div class="controls">
											<s:select list="listaTipoChiusura" id="tipoChiusura" name="tipoChiusura" cssClass="lbTextSmall span6" disabled="true" headerKey="" headerValue="" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="percentualeProRataProRataEChiusuraGruppoIva" class="control-label">&#37; Pro-rata *</label>
										<div class="controls">
											<s:textfield id="percentualeProRataProRataEChiusuraGruppoIva" name="proRataEChiusuraGruppoIva.percentualeProRata" cssClass="lbTextSmall span2 soloNumeri decimale" disabled="true" data-maintain="" />
										</div>
									</div>
									<div class="control-group">
										<label for="ivaPrecedenteProRataEChiusuraGruppoIva" class="control-label">Iva a credito precedente (negativa)</label>
										<div class="controls">
											<s:textfield id="ivaPrecedenteProRataEChiusuraGruppoIva" name="proRataEChiusuraGruppoIva.ivaPrecedente" cssClass="lbTextSmall span6 soloNumeri decimale" disabled="true" data-maintain="" />
										</div>
									</div>
								</fieldset>
							</div>
						</div>
						
						<p class="margin-medium">
							<s:include value="/jsp/include/indietro.jsp" />
							<button type="button" class="btn btn-secondary reset">annulla</button>
							<s:submit cssClass="btn btn-primary pull-right" value="aggiorna" />
						</p>
					</s:form>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
</body>
</html>