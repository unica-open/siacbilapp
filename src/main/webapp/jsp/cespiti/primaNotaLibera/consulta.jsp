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
				<%--<s:form cssClass="form-horizontal" action="%{urlStep1}" novalidate="novalidate" id="formConsultaPrimaNotaLibera" action="consultaPrimaNotaLiberaINV_valida" > --%>
				<s:form cssClass="form-horizontal"  novalidate="novalidate" id="formConsultaPrimaNotaLibera" action="consultaPrimaNotaLiberaINV_valida" >
					<s:hidden name="primaNotaLibera.uid" id="uidPrimaNotaLibera" />
					<h3><s:property value="titoloRiepilogoPrimaNotaStep3" /></h3>
					<s:include value="/jsp/include/messaggi.jsp" />
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
								<ul class="htmlelt">							
									<li>
										<dfn>Evento</dfn>
										<dl><s:property value="evento"/></dl>
									</li>
									<li>
										<dfn>Stato</dfn>
										<dl><s:property value="statoOperativo"/></dl>
									</li>
									<!-- 
									<li>
										<dfn>Cespite</dfn>
										<dl>?????</dl>
									</li>
									 -->
									<li>
										<dfn>Prima nota provvisoria</dfn>
										<dl><s:property value="primaNotaProvvisoria"/> </dl>
									</li>
									<li>
										<dfn>Prima nota definitiva</dfn>
										<dl><s:property value="primaNotaDefinitiva"/> </dl>
									</li>
								</ul>							
								
							</div>
						</div>
						<div class="clear"></div>
						<br />

						
						<div class="clear"></div>
						<br />
						<h4>Elenco scritture</h4>
							<table class="table table-hover tab_left" id="tabellaScritture">
								<thead>
									<tr>
										<th>Conto</th>
										<th>Descrizione</th>
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
<%--
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
--%>
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
<%-- 
								<tfoot>
									<tr>
										<th colspan="4">Totale</th>
										<th class="tab_Right" id="totaleDare"><s:property value="totaleDare"/></th>
										<th class="tab_Right" id="totaleAvere"><s:property value="totaleAvere"/></th>
									</tr>
								</tfoot>
--%>
							</table>
						</fieldset>
					</div>
					<div class="Border_line"></div>
					<p class="margin-medium">
						<s:include value="/jsp/include/indietro.jsp" />
						
						<s:if test="%{statoOperativo == 'Provvisorio'}">
							<s:submit value="valida" cssClass="btn btn-primary pull-right" />
						</s:if>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="${jspath}cespiti/primaNotaLibera/consulta.js"></script>

</body>
</html>