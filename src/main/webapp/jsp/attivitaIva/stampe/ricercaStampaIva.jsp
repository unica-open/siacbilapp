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
			<div class="span12 contentPage">
				<s:form action="effettuaRicercaStampaIva" novalidate="novalidate" method="post" cssClass="form-horizontal">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca stampe iva</h3>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<h4>Dati principali</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label for="tipoStampaIvaStampaIva" class="control-label">Tipo stampa iva *</label>
									<div class="controls">
										<s:select list="listaTipoStampaIva" cssClass="span6" required="true" name="stampaIva.tipoStampaIva" id="tipoStampaIvaStampaIva"
											headerKey="" headerValue="Selezionare il tipo di stampa" listValue="descrizione" />
									</div>
								</div>
								
								<div id="campiStampaIva" class="hide">
									<div class="control-group" data-registro="" data-liquidazione="" data-riepilogo="">
										<label for="annoEsercizioStampaIva" class="control-label">Anno esercizio *</label>
										<div class="controls">
											<s:textfield id="annoEsercizioStampaIva" name="annoEsercizioInt" cssClass="lbTextSmall span1 soloNumeri" maxlength="4" required="true" disabled="true" data-maintain="" />
											<s:hidden name="stampaIva.annoEsercizio" value="%{annoEsercizioInt}" />
										</div>
									</div>
									<div class="control-group" data-registro="" data-liquidazione="" data-riepilogo="">
										<label for="gruppoAttivitaIvaRegistroIva" class="control-label">Gruppo attivit&agrave; iva</label>
										<div class="controls">
											<s:select list="listaGruppoAttivitaIva" id="gruppoAttivitaIvaRegistroIva" name="registroIva.gruppoAttivitaIva.uid" cssClass="span6" headerKey="" headerValue=""
												listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
											<s:hidden id="hiddenTipoChiusura" name="tipoChiusura" />
										</div>
									</div>
									<div class="control-group" data-registro="">
										<label for="tipoRegistroIvaRegistroIva" class="control-label">Tipo registro iva</label>
										<div class="controls">
											<s:select list="listaTipoRegistroIva" id="tipoRegistroIvaRegistroIva" name="registroIva.tipoRegistroIva"
												cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
										</div>
									</div>
									<div class="control-group" data-registro="">
										<label class="control-label"></label>
										<div class="controls">
											<span class="fade <s:if test="documentiPagatiVisibile">in</s:if>" id="spanDocumentiPagati">
												<span class="alRight">
													<label class="radio inline" for="flagDocumentiPagati">Documenti pagati</label>
												</span>
												<s:checkbox id="flagDocumentiPagati" name="flagDocumentiPagati" />
											</span>
											<span class="fade <s:if test="documentiIncassatiVisibile">in</s:if>" id="spanDocumentiIncassati">
												<span class="alRight">
													<label class="radio inline" for="flagDocumentiIncassati">Documenti incassati</label>
												</span>
												<s:checkbox id="flagDocumentiIncassati" name="flagDocumentiIncassati" />
											</span>
										</div>
									</div>
									<div class="control-group" data-registro="">
										<label for="registroIva" class="control-label">Registro Iva</label>
										<div class="controls">
											<s:select list="listaRegistroIva" id="registroIva" name="registroIva.uid" cssClass="span6" headerKey="" headerValue=""
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" data-overlay="" disabled="!registroIvaAbilitato" />
										</div>
									</div>
									<div class="control-group" data-registro="" data-liquidazione="">
										<label id = "labelPeriodo" for="periodoStampaIva" class="control-label"></label>
										<div class="controls">
											<s:select list="listaPeriodo" id="periodoStampaIva" name="stampaIva.periodo" required="true" disabled="!periodoAbilitato"
												cssClass="span6" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" data-overlay="" />
										</div>
									</div>
									<div class="control-group" data-registro="" data-liquidazione="" data-riepilogo="">
										<label for="nomeFile" class="control-label">Nome File</label>
										<div class="controls">
											<s:textfield id="nomeFile" name="nomeFile" cssClass="span9" />
										</div>
									</div>
									<div class="control-group" data-registro="" data-liquidazione="" data-riepilogo="">
										<label for="dataCreazioneStampaIva" class="control-label">Data caricamento</label>
										<div class="controls">
											<s:textfield id="dataCreazioneStampaIva" name="stampaIva.dataCreazione" cssClass="span9 datepicker" />
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:reset cssClass="btn btn-secondary" value="annulla" />
						<button type="submit" class="btn btn-primary pull-right" id="pulsanteStampa">cerca</button>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/attivitaIva/stampe/stampeIva.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/attivitaIva/stampe/ricerca.js"></script>

</body>
</html>