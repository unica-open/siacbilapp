<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>


<s:if test="registrazioneSuSingolaQuota">
	<h4 class="step-pane">Importi</h4>
	<h4>Importo totale documento: <s:property value="documento.importo" />
		<span class="alLeft">Importo non rilevante iva: <s:property value="totaleNonRilevanteIva"/></span>
		<span class="alLeft">Importo rilevante iva: <s:property value="totaleRilevanteIva"/></span>
	</h4>
	
	<fieldset class="form-horizontal spaceBottom">	
		<h4 class="step-pane">Elenco quote rilevanti iva</h4>
		<table class="table table-hover tab_left" summary="...."  id="tabellaQuoteDatiIvaSpesa">
			<thead>
				<tr>
					<th scope="col" class="span1">Numero</th>
					<th scope="col">Impegno</th>
					<th scope="col">Capitolo</th>
					<th scope="col">Attivit&agrave; iva</th>
					<th scope="col">Registrazione iva</th>
					<th scope="col" class="tab_Right">Importo quota</th>
					<th scope="col" class="tab_Right">Importo totale movimenti iva</th>
					<th scope="col" class="tab_Right span1">&nbsp;</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</fieldset>
</s:if>
<s:else>
	
	<h4> <s:property value="documento.tipoDocumento.codice"/>&nbsp;
		 <s:property value="documento.anno"/>/<s:property value="documento.numero"/> &nbsp; - &nbsp;
		 <s:property value="documento.soggetto.codiceSoggetto"/> &nbsp; - &nbsp; 
		 <s:property value="documento.soggetto.denominazione"/>
	</h4>

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
							<dl><s:property value="documento.soggetto.codiceSoggetto"/>&nbsp;</dl>
						</li>
						<li>
							<dfn>Codice Fiscale</dfn>
							<dl><s:property value="documento.soggetto.codiceFiscale"/>&nbsp;</dl>
						</li>
						<li>
							<dfn>Partita IVA</dfn>
							<dl><s:property value="documento.soggetto.partitaIva"/>&nbsp;</dl>
						</li>
						<li>
							<dfn>Denominazione</dfn>
							<dl><s:property value="documento.soggetto.denominazione"/>&nbsp;</dl>
						</li>
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
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.progressivoIVA"/>&nbsp;</span>
								<span> - </span>
								<b>Anno</b>
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.annoEsercizio"/>&nbsp;</span>
							</dl></li>
						<li><dfn>Tipo registrazione</dfn>
							<dl><s:property value="documentoIvaSuInteroDocumento.tipoRegistrazioneIva.codice"/> - 
							<s:property value="documentoIvaSuInteroDocumento.tipoRegistrazioneIva.descrizione"/>
							</dl></li>
						<li><dfn>Tipo registro iva</dfn>
							<dl><s:property value="documentoIvaSuInteroDocumento.registroIva.tipoRegistroIva.codice"/> - 
							<s:property value="documentoIvaSuInteroDocumento.registroIva.tipoRegistroIva.descrizione"/>&nbsp;
							</dl></li>
						<li><dfn>Attivit&agrave;</dfn>
							<dl><s:property value="documentoIvaSuInteroDocumento.attivitaIva.codice"/> - 
							<s:property value="documentoIvaSuInteroDocumento.attivitaIva.descrizione"/>
							</dl></li>
						<li>
							<dfn>Rilevante IRAP</dfn>
							<dl>
								<s:if test="documentoIvaSuInteroDocumento.flagRilevanteIRAP">S&iacute;</s:if>
								<s:else>No</s:else>
							</dl>
						</li>
						<li><dfn>Registro</dfn>
							<dl><s:property value="documentoIvaSuInteroDocumento.registroIva.codice"/> - 
							 	<s:property value="documentoIvaSuInteroDocumento.registroIva.descrizione"/> 
							</dl></li>
						<li><dfn>Protocollo provvisorio</dfn>
							<dl>
								<b>Numero</b>
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.numeroProtocolloProvvisorio"/>&nbsp;</span>
								<span> - </span>
								<b>In data</b>
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.dataProtocolloProvvisorio"/>&nbsp;</span>
							</dl></li>
						<li><dfn>Protocollo definitivo</dfn>
							<dl>
								<b>Numero</b>
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.numeroProtocolloDefinitivo"/>&nbsp;</span>
								<span> - </span>
								<b>In data</b>
								<span class="datiIns"><s:property value="documentoIvaSuInteroDocumento.dataProtocolloDefinitivo"/>&nbsp;</span>
							</dl></li>
						<li>
							<dfn>Descrizione</dfn>
							<dl><s:property value="documentoIvaSuInteroDocumento.descrizioneIva"/>&nbsp;</dl>
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
								<s:property value="documento.totaleImportoNonRilevanteIvaQuote"/>&nbsp;
							</dl>
						</li>
						<li><dfn>Importo rilevante iva</dfn>
							<dl>
								<s:property value="documento.totaleImportoRilevanteIvaQuote"/>&nbsp;
							</dl>
						</li>
					</ul>
				</div>
			</div>
		</fieldset>
		
		<s:if test="%{documentoIvaSuInteroDocumento.listaQuoteIvaDifferita != null && documentoIvaSuInteroDocumento.listaQuoteIvaDifferita.size() > 0}">
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
					 	<s:iterator var="quotaIvaDifferita" value="documentoIvaSuInteroDocumento.listaQuoteIvaDifferita"> <%-- SubdocumentoIva --%>
							<tr>
								<td><s:property value="%{#quotaIvaDifferita.progressivoIVA}"/>&nbsp;</td>
								<td><s:property value="%{#quotaIvaDifferita.numeroOrdinativoDocumento}"/>&nbsp;</td>
								<td><s:property value="%{#quotaIvaDifferita.dataOrdinativoDocumento}"/>&nbsp;</td>
								<td><s:property value="%{#quotaIvaDifferita.dataRegistrazione}"/>&nbsp;</td>
								<td>
									<a data-content="<s:property value="%{#quotaIvaDifferita.dataProtocolloDefinitivo}"/>" 
											rel="popover" data-trigger="hover" 
											href="#" data-original-title="Data Prot. definitivo">
										<s:property value="%{#quotaIvaDifferita.numeroProtocolloDefinitivo}"/>
									</a>
								</td>
								<td class="tab_Right"><s:property value="%{#quotaIvaDifferita.totaleMovimentiIva}"/></td>
								<td class="tab_Right">
									<div class="btn-group">
										<button class="btn dropdown-toggle" data-toggle="dropdown">
											Azioni&nbsp;<span class="caret"></span>
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
				<s:include value="/jsp/documento/consultazione/consultaDocSpesa_modaleDettaglioDatiIva.jsp" />
			</div>
			
			<br/>
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
				
				<s:iterator var="aliquota" value="documentoIvaSuInteroDocumento.listaAliquotaSubdocumentoIva">  <%-- AliquotaSubdocumentoIva --%>
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
						<th class="tab_Right"><s:property value="documentoIvaSuInteroDocumento.totaleImponibileMovimentiIva"/></th>
						<th class="tab_Right"><s:property value="documentoIvaSuInteroDocumento.totaleImpostaMovimentiIva"/></th>
						<th class="tab_Right">&nbsp;</th>
						<th class="tab_Right">&nbsp;</th>
						<th class="tab_Right"><s:property value="documentoIvaSuInteroDocumento.totaleTotaleMovimentiIva"/></th>
					</tr>
				</tfoot>
			</table>
			
		</fieldset>
	
</s:else>
