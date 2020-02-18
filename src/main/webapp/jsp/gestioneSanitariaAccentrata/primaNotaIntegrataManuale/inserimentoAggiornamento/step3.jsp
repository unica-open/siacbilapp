<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="%{urlStep3}" novalidate="novalidate" id="formInserisciPrimaNotaIntegrataManuale_step3">
					<s:include value="/jsp/include/messaggi.jsp" />
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNotaLibera" />
					
					<h3><s:property value="titoloRiepilogoPrimaNotaStep3" /></h3>
					<h4>Descrizione <s:property value="primaNotaLibera.descrizione" /></h4>
					<div id="MyWizard" class="wizard">
						<ul class="steps">
							<li data-target="#step1" class="complete"><span class="badge badge-success">1</span>inserimento prima nota<span class="chevron"></span></li>
							<li data-target="#step2" class="complete"><span class="badge badge-success">2</span>dettaglio scritture<span class="chevron"></span></li>
							<li data-target="#step3" class="active"><span class="badge">3</span>riepilogo<span class="chevron"></span></li>
						</ul>
					</div>
					<div class="step-content">
						<div class="step-pane active" id="step1">
							<fieldset class="form-horizontal">
								<h4 class="nostep-pane"><span class="tlt-info"><s:property value="stringaRiepiloCausaleEPStep1" /></span></h4>
								<s:include value="/jsp/gestioneSanitariaAccentrata/primaNotaIntegrataManuale/include/accordionConsultaMovimentoFinanziario.jsp" />
								<div id="accordionPrimeNoteCollegate" class="accordion">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#divPrimeNoteCollegate" data-parent="#accordionPrimeNoteCollegate" data-toggle="collapse" class="accordion-toggle collapsed">
												Prime note collegate
												<s:if test="%{primaNotaLibera.listaPrimaNotaFiglia != null && primaNotaLibera.listaPrimaNotaFiglia.size()>0}">
												 	<span>: totale &nbsp;<s:property value="primaNotaLibera.listaPrimaNotaFiglia.size()" /></span>
												</s:if>
												<span class="icon">&nbsp;</span>
											</a>
										</div>
										<div class="accordion-body collapse" id="divPrimeNoteCollegate">
											<div class="accordion-inner">
												<table class="table table-hover tab_left" id="tabellaPrimeNoteCollegate">
													<thead>
														<tr>
															<th>Tipo</th>
															<th>Anno</th>
															<th>Numero</th>
															<th>Motivazione</th>
														</tr>
													</thead>
													<tbody>
													<s:iterator value="primaNotaLibera.listaPrimaNotaFiglia" var="pNotaCollegata">
														<tr>
															<td><s:property value="#pNotaCollegata.tipoCausale.descrizione" />
															 </td>
															<td><s:property value="#pNotaCollegata.bilancio.anno" /></td>
															<td><s:property value="#pNotaCollegata.numeroRegistrazioneLibroGiornale" />
															<td>
																<s:if test='%{#pNotaCollegata.tipoRelazionePrimaNota != null}'>
																	<s:property value="#pNotaCollegata.tipoRelazionePrimaNota.descrizione" />
																</s:if>
															</td>
														</tr>
													</s:iterator>
													</tbody>
												</table>
											</div>
										</div>
									</div>
								</div>
								
								<s:if test="validazione">
									<div class="control-group">
										<label class="control-label">Classificatori</label>
										<div class="controls">
											<div id="classGSAParent" class="accordion span9 classGSA">
												<div class="accordion-group">
													<div class="accordion-heading">
														<a href="#classGSA" data-toggle="collapse" data-parent="#classGSAParent" class="accordion-toggle collapsed">
															<span id="SPAN_classificatoreGSA">Seleziona il classificatore</span>
														</a>
													</div>
													<div id="classGSA" class="accordion-body collapse">
														<div class="accordion-inner">
															<ul id="classGSATree" class="ztree"></ul>
															<button type="button" class="btn pull-right" data-deseleziona-ztree="classGSATree">Deseleziona</button>
														</div>
													</div>
												</div>
											</div>
										</div>
										<s:hidden id="HIDDEN_classificatoreGSAUid" name="primaNotaLibera.classificatoreGSA.uid" />
									</div>
									<div class="control-group">
										<label class="control-label" for="dataRegistrazionePrimaNotaLibera">Data registrazione definitiva *</label>
										<div class="controls">
											<s:textfield id="dataRegistrazionePrimaNotaLibera" name="primaNotaLibera.dataRegistrazioneLibroGiornale" cssClass="lbTextSmall span2 datepicker" size="10" required="required"/>
										</div>
									</div>
								</s:if>
								
								<h4>Elenco scritture</h4>
								<table class="table table-hover tab_left" id="tabellaScritture">
									<thead>
										<tr>
											<th>Conto</th>
											<th>Descrizione</th>
											<th>Missione</th>
											<th>Programma</th>
											<th class="tab_Right">Dare</th>
											<th class="tab_Right">Avere</th>
										</tr>
									</thead>
									<tbody>
										<s:iterator value="primaNotaLibera.listaMovimentiEP" var="movEP">
											<s:iterator value="#movEP.listaMovimentoDettaglio" var="movDett">
												<tr>
													<td><s:property value="#movDett.conto.codice" /></td>
													<td><s:property value="#movDett.conto.descrizione" /></td>
													<td>
														<s:if test="%{#movDett.missione != null && #movDett.missione.uid != 0}">
															<s:property value="#movDett.missione.codice" /> - <s:property value="#movDett.missione.descrizione" />
														</s:if>
													</td>
													<td>
														<s:if test="%{#movDett.programma != null && #movDett.programma.uid != 0}">
															<s:property value="#movDett.programma.codice" /> - <s:property value="#movDett.programma.descrizione" />
														</s:if>
													</td>
													<td class="tab_Right"><s:property value="#movDett.importoDare" /></td>
													<td class="tab_Right"><s:property value="#movDett.importoAvere" /></td>
												</tr>
											</s:iterator>
										</s:iterator>
									</tbody>
									<tfoot>
										<tr>
											<th colspan="4">Totale</th>
											<th class="tab_Right" id="totaleDare"><s:property value="totaleDare"/></th>
											<th class="tab_Right" id="totaleAvere"><s:property value="totaleAvere"/></th>
										</tr>
									</tfoot>
								</table>
							</fieldset>
						</div>
					</div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						<s:if test="validazione">
							<s:submit value="valida" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}ztree/ztree_new.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<script type="text/javascript" src="${jspath}gestioneSanitariaAccentrata/primaNotaIntegrataManuale/inserisci.aggiorna.step3.js"></script>
	
</body>
</html>