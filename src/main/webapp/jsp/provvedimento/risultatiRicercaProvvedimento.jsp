<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />

	<!-- Corpo pagina-->

	<!-- TABELLE RIEPILOGO -->
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<form method="post" action="#">
						<s:include value="/jsp/include/messaggi.jsp" />
						<h3><span id="id_num_result" class="num_result"></span> Risultati di ricerca provvedimento</h3>
						<s:hidden id="HIDDEN_startPosition" value="%{posizioneIniziale}" />

						<table class="table table-hover" id="risultatiRicercaProvvedimento" summary="....">
							<thead>
								<tr>
									<th scope="col">Anno</th>
									<th scope="col">Numero</th>
									<th scope="col">Tipo</th>
									<th scope="col">Oggetto</th>
									<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
									<th scope="col">Stato</th>
									<th scope="col">Blocco Rag.</th>
									<th scope="col">Inserito Manualmente</th>
									<th scope="col">Azioni</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaProvvedimento" var="provvedimento">
									<tr>
										<td><s:property value="%{#provvedimento.anno}"/></td>
										<td><s:property value="%{#provvedimento.numero}"/></td>
										<td><s:property value="%{#provvedimento.tipoAtto.descrizione}"/></td>
										<td><s:property value="%{#provvedimento.oggetto}"/></td>
										<td><s:property value="%{#provvedimento.strutturaAmmContabile.codice + '-' + #provvedimento.strutturaAmmContabile.descrizione}"/></td>
										<td><s:property value="%{#provvedimento.statoOperativo}"/></td>
										<td>
											<s:if test="%{#provvedimento.bloccoRagioneria==null}">N/A</s:if>
											<s:elseif test="%{#provvedimento.bloccoRagioneria==true}">Si</s:elseif>
											<s:else>No</s:else>&nbsp;
										</td>
										<td>
											<s:if test="%{#provvedimento.provenienza==null}">N/A</s:if>
											<s:elseif test="%{#provvedimento.provenienza.equalsIgnoreCase('MANUALE')}">Si</s:elseif>
											<s:else>No</s:else>
										</td>
										<td>
											<div class="btn-group">
												<button class="btn dropdown-toggle" data-toggle="dropdown">
													Azioni <span class="caret"></span>
												</button>
												<ul class="dropdown-menu pull-right">
													<s:if test="%{(#provvedimento.bloccoRagioneria!=true && ( #provvedimento.provenienza==null ||(#provvedimento.provenienza.equalsIgnoreCase('MANUALE')))) }">
														<li><a href="risultatiRicercaProvvedimentoAggiorna.do?uidDaAggiornare=<s:property value='%{#provvedimento.uid}'/>">aggiorna</a></li>
													</s:if>
													<li><a href="risultatiRicercaProvvedimentoConsulta.do?uidDaConsultare=<s:property value='%{#provvedimento.uid}'/>">consulta</a></li>
												</ul>
											</div>
										</td>
									</tr>
								</s:iterator>
							</tbody>
							<tfoot>
							</tfoot>
						</table>
						<p>
							<s:include value="/jsp/include/indietro.jsp" />
							<s:reset cssClass="btn btn-link" value="annulla" />
						</p>
					</form>
				</div>
			</div>
		</div>
	</div>
	
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	
	<script type="text/javascript" src="${jspath}provvedimento/risultatiRicerca.js"></script>
</body>
</html>