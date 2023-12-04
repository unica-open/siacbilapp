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
				<div class="span12 contentPage">
					<s:form action="ricercaRegistrazioneMassivaMovFinFIN_effettuaRicercaRegistrazioneMovFin" cssClass="form-horizontal" novalidate="novalidate">
						<s:include value="/jsp/include/messaggi.jsp" />
						<s:hidden nam="HIDDEN_ambito" name="ambito" />
						<s:hidden name="baseUrl" id="baseUrl" />
						<s:hidden nam="HIDDEN_uidTipoEventoDaRicerca" name="uidTipoEventoDaRicerca" id="uidTipoEventoDaRicerca"/>
						<s:hidden nam="HIDDEN_uidEventoDaRicerca" name="uidEventoDaRicerca" id="uidEventoDaRicerca" />
						
						<h3>Ricerca Registro</h3>
						<p>&Egrave; necessario inserire almeno un criterio di ricerca.</p>
						<div class="step-content">
							<div class="step-pane active" id="step1">
								<fieldset class="form-horizontal">
									<p><s:submit cssClass="btn btn-primary pull-right" value="cerca" /></p>
									<h4 class="step-pane">Dati</h4>
									
									<div class="control-group">
										<label class="control-label">Tipo Elenco *</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline">
													<input type="radio" value="E" name="tipoElenco" <s:if test='%{"E".equals(tipoElenco)}'>checked</s:if>>Entrate
												</label>
											</span>
											<span class="al">
												<label class="radio inline">
													<input type="radio" value="S" name="tipoElenco" <s:if test='%{"S".equals(tipoElenco)}'>checked</s:if>>Spese
												</label>
											</span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="tipoEvento">Tipo evento *</label>
										<div class="controls">
											<s:select list="listaTipiEvento" name="tipoEvento.uid" id="tipoEvento" headerKey="" headerValue="" cssClass="span6" required="true"
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled='%{!"E".equals(tipoElenco) && !"S".equals(tipoElenco)}' />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="evento">Evento</label>
										<div class="controls">
											<s:select list="listaEventi" name="evento.uid" id="evento" headerKey="" headerValue="" cssClass="span6" required="true"
												listValue="%{codice + ' - ' + descrizione}" listKey="uid" disabled="%{evento == null || evento.tipoEvento == null || evento.tipoEvento.uid == 0}" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Movimento finanziario</label>
										<div class="controls">
											<s:textfield id="anno" cssClass="span1 soloNumeri" name="annoMovimento"  maxlength="4" placeholder="anno" />
											<s:textfield id="numero" cssClass="span2 " name="numeroMovimento"  placeholder="numero" />
											<s:textfield id="sub" cssClass="span2 soloNumeri" name="numeroSubmovimento" placeholder="sub" />
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label">Data registrazione</label>
										<div class="controls">
											<span class="al">
												<label class="radio inline">Da</label>
											</span>
											<span><s:textfield id="dataRegistrazioneDa" name="dataRegistrazioneDa" cssClass="span2 datepicker" /></span>
											<span class="al">
												<label class="radio inline">A</label>
											</span>
											<span><s:textfield id="dataRegistrazioneA" name="dataRegistrazioneA" cssClass="span2 datepicker" /></span>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="statoOperativoRegistrazione">Stato</label>
										<div class="controls">
											<select id="statoOperativoRegistrazione" disabled>
												<option>N - Notificato</option>
											</select>
										</div>
									</div>
									
									<div class="control-group">
										<label class="control-label" for="conto">Codice Conto Finanziario</label>
										<div class="controls">
											<s:textfield id="conto" cssClass="span6" name="registrazioneMovFin.elementoPianoDeiContiAggiornato.codice"/>
											<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataContoFIN">compilazione guidata</button>
										</div>
									</div>
								</fieldset>
								
																<!-- SIAC-5799 -->	
								<h4 class="step-pane">&nbsp;Provvedimento&nbsp;
									<span id="SPAN_InformazioniProvvedimento"><s:property value="descrizioneCompletaAttoAmministrativo" /></span>
								</h4>
								<div class="control-group">
									<label class="control-label" for="annoProvvedimento">Anno</label>
									<div class="controls">
										<s:textfield id="annoProvvedimento" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" maxlength="4" />
										<span class="al">
											<label class="radio inline" for="numeroProvvedimento">Numero</label>
										</span>
										<s:textfield id="numeroProvvedimento" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
										<span class="al">
											<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
										</span>
										<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="attoAmministrativo.tipoAtto.uid" id="tipoAttoProvvedimento" cssClass="span4"
											headerKey="" headerValue="" />
										<s:hidden id="HIDDEN_statoProvvedimento" name="attoAmministrativo.statoOperativo" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa</label>
									<div class="controls">
										<div class="accordion span8 struttAmm">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#struttAmm">
														<span id="SPAN_StrutturaAmministrativoContabile"> Seleziona la Struttura amministrativa </span>
														<i class="icon-spin icon-refresh spinner"></i>
													</a>
												</div>
												<div id="struttAmm" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="treeStruttAmm" class="ztree treeStruttAmm"></ul>
														<button type="button" id="pulsanteDelesezionaStrutturaAmministrativoContabile"  class="btn">Deseleziona</button>
													</div>
												</div>
											</div>
										</div>

										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileUid" name="attoAmministrativo.strutturaAmmContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileCodice" name="attoAmministrativo.strutturaAmmContabile.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileDescrizione" name="attoAmministrativo.strutturaAmmContabile.descrizione" />
									</div>
								</div>
								
							</div>
						</div>
						<p class="margin-large">
							<s:include value="/jsp/include/indietro.jsp" />
							<s:reset cssClass="btn btn-link" value="annulla" />
							<s:submit cssClass="btn btn-primary pull-right" value="cerca" />
						</p>
					</s:form>
					<s:include value="/jsp/contabilitaGenerale/include/modaleRicercaContoFIN.jsp" />
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />
					
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
	<script type="text/javascript" src="/siacbilapp/js/local/documento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale_doc.js"></script>
	
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/ricercaContoFIN.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/contabilitaGenerale/registrazione/ricercaRegistrazioneMovFin.js"></script>

</body>
</html>