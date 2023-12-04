<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<s:hidden id="HIDDEN_anno_datepicker" value="%{annoEsercizioInt}" />
	<div class="container-fluid">
		<div class="row-fluid">

			<div class="span12 contentPage">

				<form class="form-horizontal">

					<s:include value="/jsp/include/messaggi.jsp" />					
					<h3>${documento.descAnnoNumeroTipoDoc} - ${soggetto.codiceSoggetto} -  ${soggetto.denominazione}</h3>

					<fieldset class="form-horizontal">
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Dati documento di riferimento</p>
								<ul class="htmlelt">
									<li>
										<dfn>Tipo documento</dfn>
										<dl><s:property value="documento.tipoDocumento.codice"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Anno</dfn>
										<dl><s:property value="documento.anno"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Numero</dfn>
										<dl><s:property value="documento.numero"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Data emissione</dfn>
										<dl><s:property value="documento.dataEmissione"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Data scadenza</dfn>
										<dl><s:property value="documento.dataScadenza"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="documento.descrizione"/>&nbsp;</dl>
									</li>
								</ul>
							</div>

							<div class="boxOrInRight">
								<p>Soggetto</p>
								<ul class="htmlelt">
									<li>
										<dfn>Codice</dfn>
										<dl><s:property value="soggetto.codiceSoggetto"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Codice Fiscale</dfn>
										<dl><s:property value="soggetto.codiceFiscale"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Partita IVA</dfn>
										<dl><s:property value="soggetto.partitaIva"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Denominazione</dfn>
										<dl><s:property value="soggetto.denominazione"/>&nbsp;</dl>
									</li>
									<s:if test="{uidQuotaDocumentoCollegato != null && uidQuotaDocumentoCollegato != 0}">
										<li>
											<dfn>Sede secondaria</dfn>
											<dl><s:property value="subdocumento.sedeSecondariaSoggetto.denominazione"/>&nbsp;</dl>
										</li>
									</s:if>
								</ul>
							</div>

						</div>

					</fieldset>

					<fieldset class="form-horizontal">
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Dati iva</p>
								<ul class="htmlelt">
									<li><dfn>Registrazione</dfn>
										<dl>
											<b>Numero</b>
											<span class="datiIns"><s:property value="subdocumentoIva.progressivoIVA"/>&nbsp;</span>
											<span> - </span>
											<b>Anno</b>
											<span class="datiIns"><s:property value="subdocumentoIva.annoEsercizio"/>&nbsp;</span>
										</dl></li>
									<li><dfn>Tipo registrazione</dfn>
										<dl><s:property value="datiTipoRegistrazione"/>&nbsp;
										</dl></li>
									<li><dfn>Tipo registro iva</dfn>
										<dl><s:property value="datiTipoRegistroIva"/>&nbsp;
										</dl></li>
									<li><dfn>Attivit&agrave;</dfn>
										<dl><s:property value="datiAttivita"/>&nbsp;
										</dl></li>
									<li>
										<dfn>Rilevante IRAP</dfn>
										<dl>
											<s:if test="subdocumentoIva.flagRilevanteIRAP">S&iacute;</s:if>
											<s:else>No</s:else>
										</dl>
									</li>
									<li><dfn>Registro</dfn>
										<dl><s:property value="datiRegistroIva"/>&nbsp;</dl>
									</li>
									<li><dfn>Protocollo provvisorio</dfn>
										<dl>
											<b>Numero</b>
											<span class="datiIns"><s:property value="subdocumentoIva.numeroProtocolloProvvisorio"/>&nbsp;</span>
											<span> - </span>
											<b>In data</b>
											<span class="datiIns"><s:property value="subdocumentoIva.dataProtocolloProvvisorio"/>&nbsp;</span>
										</dl></li>
									<li><dfn>Protocollo definitivo</dfn>
										<dl>
											<b>Numero</b>
											<span class="datiIns"><s:property value="subdocumentoIva.numeroProtocolloDefinitivo"/>&nbsp;</span>
											<span> - </span>
											<b>In data</b>
											<span class="datiIns"><s:property value="subdocumentoIva.dataProtocolloDefinitivo"/>&nbsp;</span>
										</dl></li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="subdocumentoIva.descrizioneIva"/>&nbsp;</dl>
									</li>
								</ul>
							</div>

							<div class="boxOrInRight">
								<p>Importi</p>
								<ul class="htmlelt">
									<li><dfn>Importo totale documento</dfn>
										<dl>
											<s:property value="documento.importo"/>&nbsp;
										</dl>
									</li>
									<li><dfn>Importo non rilevante iva</dfn>
										<dl>
											<s:property value="%{documento.calcolaImportoTotaleNonRilevanteIVASubdoumenti()}"/>&nbsp;
										</dl>
									</li>
									<li><dfn>Importo rilevante iva</dfn>
										<dl>
											<s:property value="%{documento.calcolaImportoTotaleRilevanteIVASubdoumenti()}"/>&nbsp;
										</dl>
									</li>

								</ul>
							</div>

						</div>

					</fieldset>
					<s:if test="%{subdocumentoIva.listaQuoteIvaDifferita != null && subdocumentoIva.listaQuoteIvaDifferita.size() > 0}">
					<s:hidden id = "HIDDEN_tipoEntrataSpesa" name="tipoEntrataSpesa"/>
						<fieldset class="form-horizontal">
							<h4 class="step-pane">Dettaglio quote pagate</h4>
							<table id="quotePagateTable" class="table table-hover tab_left">
								<thead>
									<tr>
										<th>Numero</th>
										<th>Numero ordinativo</th>
										<th>Data ordinativo</th>
										<th>Data pagamento</th>
										<th>Protocollo definitivo</th>
										<th class="tab_Right">Importo</th>
										<th class="tab_Right">&nbsp;</th>
									</tr>
								</thead>
	
								<tbody>
								 	<s:iterator var="quotaIvaDifferita" value="subdocumentoIva.listaQuoteIvaDifferita"> <%-- SubdocumentoIva --%>
										<tr>
											<td><s:property value="%{#quotaIvaDifferita.progressivoIVA}"/>&nbsp;</td>
											<td><s:property value="%{#quotaIvaDifferita.numeroOrdinativoDocumento}"/>&nbsp;</td>
											<td><s:property value="%{#quotaIvaDifferita.dataOrdinativoDocumento}"/>&nbsp;</td>
											<td><s:property value="%{#quotaIvaDifferita.dataRegistrazione}"/>&nbsp;</td>
											<td>
												<a data-content="<s:property value="%{#quotaIvaDifferita.dataProtocolloDefinitivo}"/>" 
														data-toggle="popover" data-trigger="hover" 
														href="#" data-original-title="Data Prot. definitivo">
													<s:property value="%{#quotaIvaDifferita.numeroProtocolloDefinitivo}"/>
												</a>
											</td>
											<td class="tab_Right"><s:property value="%{#quotaIvaDifferita.totaleMovimentiIva}"/></td>
											<td class="tab_Right">
												<div class="btn-group">
													<button class="btn dropdown-toggle" data-toggle="dropdown">
														Azioni<span class="caret"></span>
													</button>
													<ul class="dropdown-menu pull-right">
														<li>
															<a href="#quotaIvaDifferita" class="dettaglioRegistrazione" data-uid='<s:property value="%{#quotaIvaDifferita.uid}" />'>dettaglio registrazione iva
															</a>
														</li>
													</ul>
												</div>
											</td>
										</tr>
	 								</s:iterator>
	
								</tbody>
							</table>
	
						</fieldset>
						<div id="quotaIvaDifferita" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettRegistrazioneLabel" aria-hidden="true">
							<s:include value="/jsp/documentoIva/consultaDocumentoIva_modaleDettaglioDatiIva.jsp" />
						</div>	
								
						<br>	
						<div class="Border_line"></div>
					
					</s:if>


					<fieldset class="form-horizontal">

						<h4 class="step-pane">Movimenti iva</h4>
						<table id="movimentiIvaTable" class="table table-hover tab_left">
							<thead>
								<tr>
									<th>Aliquota iva</th>
									<th>%</th>
									<th class="tab_Right">Imponibile</th>
									<th class="tab_Right">Imposta</th>
									<th class="tab_Right">Importo detraibile</th>
									<th class="tab_Right">Importo indetraibile</th>
									<th class="tab_Right">Totale</th>
								</tr>
							</thead>

							<tbody>
							
							<s:iterator var="aliquota" value="subdocumentoIva.listaAliquotaSubdocumentoIva">  <%-- AliquotaSubdocumentoIva --%>
								<tr>
									<td><s:property value="%{#aliquota.aliquotaIva.codice}"/> - <s:property value="%{#aliquota.aliquotaIva.descrizione}"/></td>
									<td><s:property value="%{#aliquota.aliquotaIva.percentualeAliquota}"/></td>
									<td class="tab_Right"><s:property value="%{#aliquota.imponibile}"/></td>
									<td class="tab_Right"><s:property value="%{#aliquota.imposta}"/></td>
									<td class="tab_Right"><s:property value="%{#aliquota.impostaDetraibile}"/></td>
									<td class="tab_Right"><s:property value="%{#aliquota.impostaIndetraibile}"/></td>
									<td class="tab_Right"><s:property value="%{#aliquota.totale}"/></td>
								</tr>
							</s:iterator>							

							</tbody>
							<tfoot>
								<tr>
									<th colspan="2">Totali</th>
									<th class="tab_Right"><s:property value="imponibileTotaleMovimentiIva"/></th>
									<th class="tab_Right"><s:property value="impostaTotaleMovimentiIva"/></th>
									<th class="tab_Right">&nbsp;</th>
									<th class="tab_Right">&nbsp;</th>
									<th class="tab_Right"><s:property value="totaleTotaleMovimentiIva"/></th>
								</tr>
							</tfoot>
						</table>
						
					</fieldset>

				



					<div class="Border_line"></div>
					<p>
						<a class="btn btn-secondary" href="javascript:history.go(-1)">indietro</a>
					</p>


				</form>
			</div>




		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/documentoIva/consulta.js"></script>

</body>
</html>