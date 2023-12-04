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

	<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" data-maintain="" />
	<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form action="effettuaRicercaCausaleSpesa" id="formRicercaCausaleSpesa" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Ricerca causale pagamento</h3>
					<p>&Eacute; necessario inserire almeno un criterio di ricerca.</p>
					<div class="step-content">
						<div id="step1" class="step-pane active">
							<br>
							<p>
								<span class="pull-right">
									<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
								</span>
							</p>
							<br>
							<h4>Dati causale</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa</label>
									<div class="controls">
										<div class="accordion span9 struttAmm">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a class="accordion-toggle" href="#struttAmm" data-toggle="collapse">
														<span id="SPAN_StrutturaAmministrativoContabile">Seleziona la Struttura amministrativa</span>
													</a>
												</div>
												<div id="struttAmm" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
													</div>
												</div>
											</div>
										</div>
																
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="strutturaAmministrativoContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="strutturaAmministrativoContabile.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="strutturaAmministrativoContabile.descrizione" />
									</div>
								</div>
								<div class="control-group">
									<label for="tipoCausale" class="control-label">Tipo causale</label>
									<div class="controls">
										<s:select list="listaTipoCausale" name="tipoCausale.uid" cssClass="span9" required="true" headerKey="0" headerValue=""
											listKey="uid" listValue="%{codice + '-' + descrizione}" id="tipoCausale" />
									</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="codiceCausale">Causale </label>
										<div class="controls">
											<s:textfield id="codiceCausale" cssClass="span2" name="causale.codice" placeholder="codice"/>
											<s:textfield id="descrizioneCausale" cssClass="span7" name="causale.descrizione" placeholder="descrizione"/>
										</div>
								</div>
								<div class="control-group">
									<label class="control-label" for="statoOperativoCausale">Stato causale </label>
									<div class="controls">
										<s:select id="statoOperativoCausale" list="listaStatiCausale" name="statoOperativoCausale"  required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
									</div>
								</div>
							</fieldset>
							<div id="accordionImputazioniContabili" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#collapseImputazioniContabili" data-parent="#accordionImputazioniContabili" data-toggle="collapse" class="accordion-toggle collapsed">
											Imputazioni contabili<span class="icon">&nbsp;</span>
										</a>
									</div>
									<div class="accordion-body collapse" id="collapseImputazioniContabili">
										<s:include value="/jsp/predocumento/imputazioniContabiliPreDocumentoSpesa.jsp" />
									</div>
								</div>
							</div>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn reset">annulla</button>
						<s:submit cssClass="btn btn-primary pull-right" value="cerca"/>
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/capUscitaGestione/selezionaCapitolo_modale.jsp" />
	<s:include value="/jsp/soggetto/selezionaSoggetto_modale.jsp" />
	<s:include value="/jsp/movimentoGestione/modaleImpegno.jsp" />
	<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
	<%-- MODALE USCITA GESTIONE --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/codiceFiscale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/capitolo/ricercaCapitoloModale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/soggetto/ricerca.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/movimentoGestione/ricercaImpegnoOttimizzato.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/predocumento.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/causale/ricercaCausaleSpesa.js"></script>

</body>
</html>