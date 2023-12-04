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
				<s:hidden name="baseUrl" id="HIDDEN_baseUrl" />
				<s:form cssClass="form-horizontal" action="#" novalidate="novalidate" id="formConsultaPrimaNotaLibera">
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNota" />
					<h3><s:property value="titoloRiepilogoPrimaNotaStep3" /></h3>
					
					<div class="step-pane active" id="step1">
						<fieldset class="form-horizontal">
							<h4 class="step-pane">
								Inserimento: <s:property value="primaNotaLibera.dataCreazionePrimaNota"/> (<s:property value="primaNotaLibera.loginCreazione" />)
								<s:if test="%{primaNotaLibera.dataModificaPrimaNota != null}">
									- Ultima modifica: <s:property value="primaNotaLibera.dataModificaPrimaNota"/> (<s:property value="primaNotaLibera.loginModifica" />)
								</s:if>
							</h4>
							<div class="boxOrSpan2">
								<div class="boxOrInline">
									<p>Dati Causale</p>
									<ul class="htmlelt">
										<li>
											<dfn>Causale</dfn>
											<dl><s:property value="descrizioneCausale"/>&nbsp;</dl>
										</li>
										<li>
											<dfn>Data registrazione</dfn>
											<dl><s:property value="primaNotaLibera.dataRegistrazione"/>&nbsp;</dl>
										</li>
										<li>
											<dfn>Descrizione</dfn>
											<dl><s:property value="primaNotaLibera.descrizione"/>&nbsp;</dl>
										</li>
										<s:if test="dataRegistrazioneDefinitivaVisibile">
											<li>
												<dfn>Data registrazione definitiva</dfn>
												<dl><s:property value="primaNotaLibera.dataRegistrazioneLibroGiornale"/>&nbsp;</dl>
											</li>
										</s:if>
									</ul>
								</div>
							</div>
							<div class="clear"></div>
							<br />
							<s:include value="/jsp/gestioneSanitariaAccentrata/primaNotaIntegrataManuale/include/accordionConsultaMovimentoFinanziario.jsp" />
							<div id="accordionPrimeNoteCollegate" class="accordion">
								<div class="accordion-group">
									<div class="accordion-heading">
										<a href="#divPrimeNoteCollegate" data-parent="#accordionPrimeNoteCollegate" data-toggle="collapse" class="accordion-toggle collapsed">
											Prime note collegate
											<s:if test="%{primaNotaLibera.listaPrimaNotaFiglia != null && primaNotaLibera.listaPrimaNotaFiglia.size() > 0}">
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
														<th>Stato</th>
													</tr>
												</thead>
												<tbody>
												<s:iterator value="primaNotaLibera.listaPrimaNotaFiglia" var="pNotaCollegata">
													<tr>
														<td><s:property value="#pNotaCollegata.tipoCausale.descrizione" /></td>
														<td><s:property value="#pNotaCollegata.bilancio.anno" /></td>
														<td><s:property value="#pNotaCollegata.numeroRegistrazioneLibroGiornale" />
														<td>
															<s:if test='%{#pNotaCollegata.tipoRelazionePrimaNota != null}'>
																<s:property value="#pNotaCollegata.tipoRelazionePrimaNota.descrizione" />
															</s:if>
														</td>
														<td>
															<s:if test='%{#pNotaCollegata.statoOperativoPrimaNota != null}'>
																<s:property value="#pNotaCollegata.statoOperativoPrimaNota.descrizione" />
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
							<div class="clear"></div>
							<br />
							<div class="control-group">
								<label class="control-label">Classificatori</label>
								<div class="controls">
									<div id="classGSAParent" class="accordion span9 classGSA">
										<div class="accordion-group">
											<div class="accordion-heading">
												<a href="#" data-toggle="collapse" data-parent="#classGSAParent" class="accordion-toggle collapsed">
													<s:property value="descrizioneClassificatoreGSA"/>
												</a>
											</div>
											<div id="classGSA" class="accordion-body collapse">
											</div>
										</div>
									</div>
								</div>
								<s:hidden id="HIDDEN_classificatoreGSAUid" name="primaNota.classificatoreGSA.uid" />
							</div>
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
												<td class="tab_Right">
													<s:if test='%{"DARE".equals(#movDett.segno.name())}'>
														<s:property value="#movDett.importo" />
													</s:if>
												</td>
												<td class="tab_Right">
													<s:if test='%{"AVERE".equals(#movDett.segno.name())}'>
														<s:property value="#movDett.importo" />
													</s:if>
												</td>
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
					<div class="Border_line"></div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/ztree/ztree_new.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/classifgsa/ztree.classifgsa.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/gestioneSanitariaAccentrata/primaNotaIntegrataManuale/consulta.js"></script>


</body>
</html>