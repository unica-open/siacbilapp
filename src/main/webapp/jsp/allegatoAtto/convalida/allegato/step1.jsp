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
			<div class="span12 contentPage">
				<s:form id="formConvalidaAllegatoAttoStep1" cssClass="form-horizontal" novalidate="novalidate" action="convalidaAllegatoAtto_completeStep1" method="post">
					<s:hidden id="nomeAzioneDecentrata" value="%{nomeAzioneDecentrata}" data-maintain="" />
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Valuta atto contabile / allegato</h3>
					<div class="wizard">
						<ul class="steps">
							<li class="active" data-target="#step1"><span class="badge">1</span>Valuta atto<span class="chevron"></span></li>
							<li data-target="#step2"><span class="badge">2</span>Convalida elenchi<span class="chevron"></span></li>
						</ul>
					</div>

					<div class="step-content">
						<div id="step1" class="step-pane active">

							<h4 class="step-pane">
								Provvedimento <span id="datiRiferimentoAttoAmministrativoSpan">
									<s:if test='%{attoAmministrativo != null && (attoAmministrativo.anno ne null && attoAmministrativo.anno != "") && (attoAmministrativo.numero ne null && attoAmministrativo.numero != "") && (tipoAtto.uid ne null && tipoAtto.uid != "")}'>
										<s:property value="%{tipoAtto.descrizione+ '/'+ attoAmministrativo.anno + ' / ' + attoAmministrativo.numero + ' - ' + attoAmministrativo.oggetto}" />
									</s:if>
								</span>
							</h4>
							<fieldset class="form-horizontal">
								<div class="control-group">
									<label class="control-label" for="annoAttoAmministrativo">Anno</label>
									<div class="controls">
										<s:textfield id="annoAttoAmministrativo" cssClass="lbTextSmall span1 soloNumeri" name="attoAmministrativo.anno" />
										<span class="al">
											<label class="radio inline" for="numeroAttoAmministrativo">Numero</label>
										</span>
										<s:textfield id="numeroAttoAmministrativo" cssClass="lbTextSmall span2 soloNumeri" name="attoAmministrativo.numero" maxlength="7" />
										<span class="al">
											<label class="radio inline" for="tipoAtto">Tipo</label>
										</span>
										<s:select list="listaTipoAtto" listKey="uid" listValue="descrizione" name="tipoAtto.uid"
											id="tipoAtto" cssClass="span4" headerKey="0" headerValue="" />
										<s:hidden id="statoOperativoAttoAmministrativo" name="attoAmministrativo.statoOperativo" />
										<span class="radio guidata">
											<a href="#" id="pulsanteApriModaleProvvedimento" class="btn btn-primary">compilazione guidata</a>
										</span>
									</div>
								</div>
								<div class="control-group">
									<label class="control-label">Struttura Amministrativa</label>
									<div class="controls">
										<div class="accordion span8 struttAmm" id="accordionStrutturaAmministrativaContabileAttoAmministrativo">
											<div class="accordion-group">
												<div class="accordion-heading">
													<a class="accordion-toggle" id="accordionPadreStrutturaAmministrativa" href="#collapseStrutturaAmministrativaContabileAttoAmministrativo"
															data-parent="#accordionStrutturaAmministrativaContabileAttoAmministrativo">
														<span id="SPAN_StrutturaAmministrativoContabileAttoAmministrativo">Seleziona la Struttura amministrativa</span>
													</a>
												</div>
												<div id="collapseStrutturaAmministrativaContabileAttoAmministrativo" class="accordion-body collapse">
													<div class="accordion-inner">
														<ul id="treeStruttAmmAttoAmministrativo" class="ztree treeStruttAmm"></ul>
													</div>
												</div>
											</div>
										</div>

										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoUid" name="strutturaAmministrativoContabile.uid" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoCodice" name="strutturaAmministrativoContabileAttoAmministrativo.codice" />
										<s:hidden id="HIDDEN_StrutturaAmministrativoContabileAttoAmministrativoDescrizione" name="strutturaAmministrativoContabileAttoAmministrativo.descrizione" />
									</div>
								</div>
							</fieldset>
						</div>
					</div>

					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-secondary reset">annulla</button>
						<span class="pull-right">
							<s:submit cssClass="btn btn-primary" value="prosegui" />
						</span>
					</p>
					<s:include value="/jsp/provvedimento/selezionaProvvedimento_modale.jsp" />

				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/predocumento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_modale.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/allegatoAtto/convalidaElenco_step1.js"></script>
	
	
</body>
</html>