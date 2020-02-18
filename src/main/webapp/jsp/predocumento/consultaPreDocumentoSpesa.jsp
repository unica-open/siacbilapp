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
				<s:form cssClass="form-horizontal">
					<h3><s:property value="titoloPredisposizione" escapeHtml="false" /></h3>
					<h4 class="step-pane">
						Inserimento: <s:property value="preDocumento.dataCreazione"/> (<s:property value="preDocumento.loginCreazione"/>) - 
						Ultima modifica: <s:property value="preDocumento.dataModifica"/> (<s:property value="preDocumento.loginModifica"/>)
					</h4>
					<fieldset class="form-horizontal">
						<div class="boxOrSpan2">
							<div class="boxOrInLeft">
								<p>Dati predisposizione</p>
								<ul class="htmlelt">
									<li>
										<dfn>Competenza:</dfn>
										<dl>
											<b>Data:</b>&nbsp;<s:property value="preDocumento.dataCompetenza"/>
											<span class="alLeft">-</span>
											<span class="alLeft"><b>Periodo:</b></span>&nbsp;<s:property value="preDocumento.periodoCompetenza"/>
										</dl>
									</li>
									<li>
										<dfn><abbr title="Struttura Amministrativa Contabile">Strutt. amm. contabile</abbr></dfn>
										<dl>
											<s:if test="%{preDocumento.strutturaAmministrativoContabile != null}">
												<s:property value="preDocumento.strutturaAmministrativoContabile.codice"/> - <s:property value="preDocumento.strutturaAmministrativoContabile.descrizione"/>
											</s:if>&nbsp;
										</dl>
									</li>
									<li>
										<dfn>Causale</dfn>
										<dl>
											<s:property value="preDocumento.causaleSpesa.codice"/> - <s:property value="preDocumento.causaleSpesa.descrizione"/> -
											<s:property value="preDocumento.causaleSpesa.tipoCausale.codice"/> - <s:property value="preDocumento.causaleSpesa.tipoCausale.descrizione"/>
										</dl>
									</li>
									<li>
										<dfn>Conto del tesoriere</dfn>
										<dl><s:property value="preDocumento.contoTesoreria.codice"/> - <s:property value="preDocumento.contoTesoreria.descrizione"/></dl>
									</li>
									<li>
										<dfn>Provvisorio di cassa</dfn>
										<dl>&nbsp;</dl>
									</li>
									<li>
										<dfn>Stato</dfn>
										<dl><s:property value="preDocumento.statoOperativoPreDocumento.descrizione" /> dal <s:property value="preDocumento.dataInizioValiditaStato"/></dl>
									</li>
									<li>
										<dfn>Elenco</dfn>
										<dl><s:property value="elencoPreDocumento"/></dl>
									</li>
								</ul>

							</div>

							<div class="boxOrInRight">
								<p>Anagrafica predisposizione</p>

								<ul class="htmlelt">
									<li>
										<dfn>Denominazione</dfn>
										<dl><s:property value="denominazione" escape="false"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Codice fiscale</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.codiceFiscale"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Partita IVA</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.partitaIva"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Indirizzo</dfn>
										<dl>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.indirizzo != null && !preDocumento.datiAnagraficiPreDocumento.indirizzo.isEmpty()}">
												<b>Indirizzo:</b>&nbsp;<s:property value="preDocumento.datiAnagraficiPreDocumento.indirizzo" /><br/>
											</s:if>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.comuneIndirizzo != null && !preDocumento.datiAnagraficiPreDocumento.comuneIndirizzo.isEmpty()}">
												<b>Comune:</b>&nbsp;<s:property value="preDocumento.datiAnagraficiPreDocumento.comuneIndirizzo"/>
												<s:if test="%{preDocumento.datiAnagraficiPreDocumento.nazioneIndirizzo != null && !preDocumento.datiAnagraficiPreDocumento.nazioneIndirizzo.isEmpty()}">
													&nbsp;(<s:property value="preDocumento.datiAnagraficiPreDocumento.nazioneIndirizzo"/>)
												</s:if>
											</s:if>
										</dl>
									</li>
									<li>
										<dfn>Email</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.indirizzoEmail"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Telefono</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.numTelefono"/>&nbsp;</dl>
									</li>
								</ul>
							</div>
						</div>

						<div class="boxOrSpan2">
							<div class="boxOrInLeft ">
								<p>Estremi predisposizione pagamento</p>
								<ul class="htmlelt">
									<li>
										<dfn>Data esecuzione</dfn>
										<dl><s:property value="preDocumento.dataDocumento" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Importo</dfn>
										<dl><s:property value="preDocumento.importo" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Descrizione</dfn>
										<dl><s:property value="preDocumento.descrizione" />&nbsp;</dl>
									</li>
									<li>
										<dfn>Note</dfn>
										<dl><s:property value="preDocumento.note" />&nbsp;</dl>
									</li>
								</ul>
							</div>
							<div class="boxOrInRight ">
								<p>Modalit&agrave; pagamento</p>
								<ul class="htmlelt">
									<li>
										<dfn>Intestazione conto</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.intestazioneConto"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Conto</dfn>
										<dl>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.codiceABI != null && !preDocumento.datiAnagraficiPreDocumento.codiceABI.isEmpty()}">
												<s:property value="preDocumento.datiAnagraficiPreDocumento.codiceABI"/> /
											</s:if>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.codiceCAB != null && !preDocumento.datiAnagraficiPreDocumento.codiceCAB.isEmpty()}">
												<s:property value="preDocumento.datiAnagraficiPreDocumento.codiceCAB"/> /
											</s:if>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.contoCorrente != null && !preDocumento.datiAnagraficiPreDocumento.contoCorrente.isEmpty()}">
												<s:property value="preDocumento.datiAnagraficiPreDocumento.contoCorrente"/>
											</s:if>&nbsp;
										</dl>
									</li>
									<li>
										<dfn>IBAN</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.codiceIban"/>&nbsp;</dl>
									</li>
									<li><dfn>BIC</dfn>
										<dl><s:property value="preDocumento.datiAnagraficiPreDocumento.codiceBic"/>&nbsp;</dl>
									</li>
									<li>
										<dfn>Quietanzante</dfn>
										<dl>
											<s:property value="preDocumento.datiAnagraficiPreDocumento.soggettoQuietanzante"/>
											<s:if test="%{preDocumento.datiAnagraficiPreDocumento.soggettoQuietanzante != null && !preDocumento.datiAnagraficiPreDocumento.soggettoQuietanzante.isEmpty() &&
													preDocumento.datiAnagraficiPreDocumento.codiceFiscaleQuietanzante != null && !preDocumento.datiAnagraficiPreDocumento.codiceFiscaleQuietanzante.isEmpty()}">
												&nbsp;-&nbsp;
											</s:if>
											<s:property value="preDocumento.datiAnagraficiPreDocumento.codiceFiscaleQuietanzante"/>
										</dl>
									</li>
								</ul>
							</div>
						</div>

						<div class="boxOrSpan2">
							<div class="boxOrInLeft ">
								<p>Estremi pagamento</p>
								<ul class="htmlelt">
									<li>
										<dfn>Documento</dfn>
										<dl>
											<s:if test="%{documento.anno != null && documento.numero != null && documento.tipoDocumento != null}">
												<b>Anno:</b>&nbsp;<s:property value="documento.anno"/>
												<span class="alLeft">-</span>&nbsp;
												<b>Numero:</b>&nbsp;<s:property value="documento.numero"/>
												<span class="alLeft">-</span>&nbsp;
												<b>Tipo:</b>&nbsp;<s:property value="documento.tipoDocumento.codice"/>
											</s:if>
										</dl>
									</li>
									
									<s:if test="%{ordinativoSubCollegato != null }">
										<li>
											<dfn>Ordinativo</dfn>
											<dl><s:property value="ordinativoSubCollegato.anno"/>&nbsp;/&nbsp;<s:property value="ordinativoSubCollegato.numero"/></dl>
										</li>
										<li>
											<dfn>Capitolo ordinativo</dfn>
											<dl><s:property value="capitolo.annoCapitolo"/>&nbsp;/&nbsp;<s:property value="capitolo.numeroCapitolo"/>&nbsp;/&nbsp;<s:property value="capitolo.numeroArticolo"/></dl>
										</li>
										<li>
											<dfn>Impegno ordinativo</dfn>
											<dl><s:property value="movimentoGestioneOrdinativo.annoMovimento"/>&nbsp;/&nbsp;<s:property value="movimentoGestioneOrdinativo.numero.toString()"/></dl>
										</li>
										<li>
											<dfn>Soggetto ordinativo</dfn>										
											<s:property value="soggettoOrdinativo.codiceSoggetto"/>&nbsp;/&nbsp;<s:property value="soggettoOrdinativo.denominazione"/>
										</li>
									</s:if>
									<s:else>
										<li>
											<dfn>Ordinativo</dfn>
											<dl>&nbsp;</dl>
										</li>
										<li>
											<dfn>Capitolo ordinativo</dfn>
											<dl>&nbsp;</dl><%-- /<s:property value="capitolo.numeroUEB}"/></dl>--%>
										</li>
										<li>
											<dfn>Accertamento ordinativo</dfn>
											<dl>&nbsp;</dl>
										</li>
										<li>
											<dfn>Soggetto ordinativo</dfn>
											<dl>&nbsp;</dl>
										</li>
									</s:else>
									
								</ul>
								
							</div>
							<div class="boxOrInRight ">
								<p>Imputazioni contabili</p>
								<ul class="htmlelt">
									<li>
										<dfn>Capitolo</dfn>
										<dl>
											<s:if test="%{capitolo != null}">
												<s:property value="capitolo.annoCapitolo"/> / <s:property value="capitolo.numeroCapitolo"/> / <s:property value="capitolo.numeroArticolo"/>
												<s:if test="%{gestioneUEB}">
													/ <s:property value="capitolo.numeroUEB"/>
												</s:if> 
											</s:if>
										</dl>
									</li>
									<li>
										<dfn>Impegno</dfn>
										<dl>
											<s:if test="%{movimentoGestione != null}">
												<s:property value="movimentoGestione.annoMovimento"/> / <s:property value="movimentoGestione.numero.toString()"/>
												<s:if test="%{subMovimentoGestione != null}">
													- <s:property value="subMovimentoGestione.numero.toString()"/>
												</s:if> 
											</s:if>
										</dl>
									</li>
									<li>
										<dfn>Soggetto</dfn>
										<dl>
											<s:if test="%{soggetto != null}">
												<s:property value="soggetto.codiceSoggetto"/> / <s:property value="soggetto.denominazione"/>
											</s:if>
										</dl>
									</li>
									<li>
										<dfn>Modalit&agrave; di pagamento</dfn>
										<dl>
											<s:if test="%{preDocumento.modalitaPagamentoSoggetto != null}">
												<s:property value="preDocumento.modalitaPagamentoSoggetto.descrizioneInfo.descrizioneArricchita"/>
											</s:if>
										</dl>
									</li>
									<li>
										<dfn>Provvedimento</dfn>
										<dl>
											<s:if test="%{attoAmministrativo != null}">
												<s:property value="attoAmministrativo.anno"/> / <s:property value="attoAmministrativo.numero"/> - <s:property value="attoAmministrativo.tipoAtto.codice"/>
												<s:if test="%{attoAmministrativo.strutturaAmmContabile != null}">
													- <s:property value="attoAmministrativo.strutturaAmmContabile.codice"/>
												</s:if>
												<s:if test="%{attoAmministrativo.oggetto != null && !attoAmministrativo.oggetto.isEmpty()}">
													- <s:property value="attoAmministrativo.oggetto"/>
												</s:if>
											</s:if>
										</dl>
									</li>
								</ul>
							</div>
						</div>
					</fieldset>
					<p class="marginLarge">
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</s:form>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />

</body>
</html>