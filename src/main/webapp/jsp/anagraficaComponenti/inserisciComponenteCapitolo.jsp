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
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Inserisci Componente Capitolo</h3>
					<div class="step-content">
						<div class="step-pane active">
							<h4>Dati</h4>
							<s:form action="inserisciComponenteCapitoloAction_inserimentoCP" id="editComponenteCapitolo" novalidate="novalidate">
								<s:hidden id="HIDDEN_annoBil" value="%{bilancio.anno}" var="annoBil"/>
								<div class="fieldset-body">
									<fieldset class="form-horizontal">
										<div class="control-group">
											<label for="macroTipo" class="control-label">Macrotipo *</label>
											<div class="controls">
												<s:select list="listaMacroTipo" headerKey="" headerValue="" listValue="descrizione" name="componenteCapitolo.macrotipoComponenteImportiCapitolo"
													id="macroTipo" cssClass="lbTextSmall span10" />
											</div>
										</div>
	
										<div class="control-group">
											<label for=sottoTipo class="control-label">Sottotipo</label>
											<div class="controls">
												<s:select list="listaSottoTipo" listKey="uid" headerKey="" headerValue="" listValue="descrizione" name="componenteCapitolo.sottotipoComponenteImportiCapitolo"
													id="sottoTipo" cssClass="lbTextSmall span10" data-old-value="%{componenteCapitolo.sottotipoComponenteImportiCapitolo}" />
											</div>
										</div>
	
										<div class="control-group">
											<label for="descrizioneCapitolo" class="control-label">Descrizione *</label>
											<div class="controls">
												<s:textarea rows="1" cols="15" id="descrizioneCapitolo" name="componenteCapitolo.descrizione" class="span10" maxlength="500"></s:textarea>
											</div>
										</div>
	
										<div class="control-group">
											<label for="ambito" class="control-label">Ambito</label>
											<div class="controls">
												<s:select list="listaAmbito" listKey="uid" listValue="descrizione" name="componenteCapitolo.ambitoComponenteImportiCapitolo"
													id="ambito" cssClass="lbTextSmall span10" headerKey="" headerValue="" data-old-value="%{componenteCapitolo.ambitoComponenteImportiCapitolo}" />
											</div>
										</div>
	
										<div class="control-group">
											<label for="fonteFinanziamento" class="control-label">Fonte di finanziamento</label>
											<div class="controls">
												<s:select list="listaFonteFinanziamento" listKey="uid" headerKey="" disabled="false" headerValue="" listValue="descrizione"
													name="componenteCapitolo.fonteFinanziariaComponenteImportiCapitolo" id="fonteFinanziamento" cssClass="lbTextSmall span10"
													data-old-value="%{componenteCapitolo.fonteFinanziariaComponenteImportiCapitolo}" />
											</div>
										</div>
	
										<div class="control-group">
											<label for="momento" class="control-label">Momento</label>
											<div class="controls">
												<s:select list="listaMomento" listKey="uid" listValue="descrizione" name="componenteCapitolo.momentoComponenteImportiCapitolo"
													id="momento" cssClass="lbTextSmall span10" headerKey="" headerValue="" data-old-value="%{componenteCapitolo.momentoComponenteImportiCapitolo}" />
											</div>
										</div>
	
										<div class="control-group">
											<label for="annoCapitolo" class="control-label">Anno</label>
											<div class="controls">
												<s:textfield id="annoCapitolo" name="componenteCapitolo.anno" cssClass="lbTextSmall span4 soloNumeri" maxlength="4" />
												<span class="al alRight">
													<label class="radio inline" for="previsione">Default Previsione *</label>
												</span>
												<s:select list="listaPrevisione" listValue="descrizione" name="componenteCapitolo.propostaDefaultComponenteImportiCapitolo"
													id="previsione" cssClass="lbTextSmall span4" required="true" headerKey="" headerValue="" />
											</div>
										</div>
										<!-- SIAC-7349 -->
										<%--   
										<div class="control-group">
											<label for="gestione" class="control-label">Tipo Gestione *</label>
											<div class="controls">
												<s:select list="listaGestione" listValue="descrizione" name="componenteCapitolo.tipoGestioneComponenteImportiCapitolo"
													id="gestione" cssClass="lbTextSmall span10" required="true" headerKey="" headerValue="" />
											</div>
										</div>
										--%>
										<div class="control-group">
											<label for="impegnabile" class="control-label">Impegnabile *</label>
											<div class="controls">
												<s:select list="listaImpegnabile" listValue="descrizione" name="componenteCapitolo.impegnabileComponenteImportiCapitolo"
													id="impegnabile" cssClass="lbTextSmall span10" required="true" headerKey="" headerValue="" />
											</div>
										</div>
	
										<div class="control-group">
											<label for="dataInizioValiditaCapitolo" class="control-label">Data Inizio Validit&agrave; *</label>
											<div class="controls">
												<s:textfield id="dataInizioValiditaCapitolo" name="componenteCapitolo.dataInizioValidita" cssClass="span4 datepicker" maxlength="10" data-maintain="" />
												<span class="al alRight">
													<label class="radio inline" for="dataFineValiditaCapitolo">Data Fine Validit&agrave;</label>
												</span>
												<s:textfield id="dataFineValiditaCapitolo" name="componenteCapitolo.dataFineValidita" cssClass="span4 datepicker" maxlength="10" data-maintain="" />
											</div>
										</div>
									</fieldset>
								</div>
								<p class="margin-medium">
									<s:include value="/jsp/include/indietro.jsp" />
									<s:a action="annullaInserisciComponenteCapitolo" class="btn btn-secondary">annulla</s:a>
									<s:submit cssClass="btn btn-primary pull-right" value="salva" />
								</p>
							</s:form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaComponenti/componenteCapitolo.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/anagraficaComponenti/inserisci.js"></script>
</body>
</html>