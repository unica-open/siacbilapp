<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${param.suffix != null}">
	<s:set var="suffix">${param.suffix}</s:set>
</c:if>
<c:if test="${param.useSAC != null}">
	<s:set var="useSAC">${param.useSAC}</s:set>
</c:if>
<c:choose>
	<c:when test="${param.useSAC != null}">
		<s:set var="useSAC">${param.useSAC}</s:set>
	</c:when>
	<c:otherwise>
		<s:set var="useSAC">false</s:set>
	</c:otherwise>
</c:choose>
<!--modale capitolo -->
<div id="modaleGuidaCapitolo<s:property value="%{#suffix}" />" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaCapLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Seleziona capitolo</h4>
		</div>

		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_MODALE_CAPITOLO<s:property value="%{#suffix}" />">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<fieldset class="form-horizontal" id="fieldsetRicercaGuidataCapitolo<s:property value="%{#suffix}" />">
				<div id="campiRicercaCapitolo<s:property value="%{#suffix}" />" class="accordion-body collapse in">
					<div class="control-group">
						<label class="control-label" for="annoCapitolo_modale<s:property value="%{#suffix}" />">Anno</label>
						<div class="controls">
							<s:textfield id="annoCapitolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.annoCapitolo"
								disabled="true" value="%{capitolo.annoCapitolo}" data-maintain=""/>
							<s:hidden id="annoCapitolo_modaleHidden%{#suffix}" name="modale.capitolo.annoCapitolo" value="%{capitolo.annoCapitolo}"/>
							<span class="al">
								<label class="radio inline" for="numeroCapitolo_modale<s:property value="%{#suffix}" />">Capitolo*</label>
							</span>
							<s:textfield id="numeroCapitolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroCapitolo" maxlength="9" />
							<span class="al">
								<label class="radio inline" for="numeroArticolo_modale<s:property value="%{#suffix}" />">Articolo*</label>
							</span>
							<s:textfield id="numeroArticolo_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroArticolo" maxlength="9" />
							<s:if test="gestioneUEB">
								<span class="al">
									<label class="radio inline" for="numeroUEB_modale<s:property value="%{#suffix}" />">UEB</label>
								</span>
								<s:textfield id="numeroUEB_modale%{#suffix}" cssClass="lbTextSmall span2 soloNumeri" name="modale.capitolo.numeroUEB" maxlength="9" />
							</s:if><s:else>
								<s:hidden id="numeroUEB_modale%{#suffix}" name="modale.capitolo.numeroUEB" value="1" />
							</s:else>
						</div>
					</div>
					<s:if test='%{"true".equals(#useSAC)}'>
						<div class="control-group">
							<label class="control-label">Struttura Amministrativa</label>
							<div class="controls">
								<div class="accordion span9 struttAmm">
									<div class="accordion-group">
										<div class="accordion-heading">
											<a href="#struttAmm_modale<s:property value='%{#suffix}' default='' />" data-toggle="collapse" class="accordion-toggle collapsed">
												<span id="SPAN_StrutturaAmministrativoContabile_modale<s:property value='%{#suffix}' default='' />">Seleziona la Struttura amministrativa</span>
											</a>
										</div>
										<div id="struttAmm_modale<s:property value='%{#suffix}' default='' />" class="accordion-body collapse">
											<div class="accordion-inner">
												<ul id="treeStruttAmm_modale<s:property value='%{#suffix}' default='' />" class="ztree treeStruttAmm"></ul>
												<button type="button" class="btn btn-primary pull-right" data-toggle="collapse" data-target="#struttAmm_modale<s:property value='%{#suffix}' default='' />">Conferma</button>
											</div>
										</div>
									</div>
								</div>
								<a class="btn btn-primary pull-right collapsed" id="pulsanteRicercaCapitolo<s:property value="%{#suffix}" />" data-toggle="collapse">
									<i class="icon-search icon"></i>&nbsp;cerca
									&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_RicercaCapitolo<s:property value="%{#suffix}" />"></i>
								</a>
	
								<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileUid_modale' + #suffix}" name="modale.strutturaAmministrativoContabile.uid" />
								<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileCodice_modale' + #suffix}" name="modale.strutturaAmministrativoContabile.codice" />
								<s:hidden id="%{'HIDDEN_StrutturaAmministrativoContabileDescrizione_modale' + #suffix}" name="modale.strutturaAmministrativoContabile.descrizione" />
							</div>
						</div>
					</s:if>
					<s:else>
						<div class="control-group">
							<label class="control-label" for="tipoFin<s:property value="%{#suffix}" />">Tipo finanziamento</label>
							<div class="controls">
								<s:select list="listaTipiFinanziamento" cssClass="span6" name="modale.tipoFinanziamento.uid" id="tipoFin%{#suffix}"
									headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
								<a class="btn btn-primary pull-right collapsed" id="pulsanteRicercaCapitolo<s:property value="%{#suffix}" />" data-toggle="collapse">
									<i class="icon-search icon"></i>&nbsp;cerca
									&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_RicercaCapitolo<s:property value="%{#suffix}" />"></i>
								</a>
							</div>
						</div>
					</s:else>
				</div>
			</fieldset>

			<div id="divTabellaCapitoli<s:property value="%{#suffix}" />" class="hide">
				<h4>Elenco Ueb capitolo <s:property value="capitolo.numeroCapitolo"/><s:property value="capitolo.numeroUEB"/></h4>
				<table class="table table-hover" id="risultatiRicercaCapitolo<s:property value="%{#suffix}" />">
					<thead>
						<tr>
							<th scope="col"></th> 
							<th scope="col">Capitolo</th>
							<th scope="col">Classificazione</th>
							<th scope="col" class="text-right">Disponibile <s:property value="%{annoEsercizioInt + 0}" /></th>
							<th scope="col" class="text-right">Disponibile <s:property value="%{annoEsercizioInt + 1}" /></th>
							<th scope="col" class="text-right">Disponibile <s:property value="%{annoEsercizioInt + 2}" /></th>
							<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt Amm Resp</abbr></th>
							<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
							<th scope="col">Tipo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					<tfoot>
					</tfoot>
				</table>

				<div id="divVisualizzaDettaglio<s:property value="%{#suffix}" />" class="hide">
					<p>
						<a class="btn" data-uncheck="checkCapitolo">deseleziona</a>
						<a class="btn btn-secondary" id="pulsanteVisualizzaDettaglio<s:property value="%{#suffix}" />">visualizza dettaglio</a>
					</p>
				</div>

				<div id="collapseDettaglioCapitolo<s:property value="%{#suffix}" />" class="collapse">
					<div class="accordion_info">
						<h4 id="informazioniCapitoloModale<s:property value="%{#suffix}" />"></h4>
						<table summary="tabellaRiepilogoIncarichi" class="table table-hover tab_left">
							<tr>
								<th>Stanziamenti</th>
								<th class="tab_Right"><s:property value="%{annoEsercizioInt}"/></th>
								<th class="tab_Right"><s:property value="%{annoEsercizioInt+1}"/></th>
								<th class="tab_Right"><s:property value="%{annoEsercizioInt+2}"/></th>
							</tr>
							<tr>
								<th>Competenza</th>
								<td class="tab_Right" id="modaleElementoSelezionatoCompetenzaAnno0<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoCompetenzaAnno1<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoCompetenzaAnno2<s:property value="%{#suffix}" />"></td>
							</tr>
							<tr>
								<th>Residuo</th>
								<td class="tab_Right" id="modaleElementoSelezionatoResiduoAnno0<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoResiduoAnno1<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoResiduoAnno2<s:property value="%{#suffix}" />"></td>
							</tr>
							<tr>
								<th>Cassa</th>
								<td class="tab_Right" id="modaleElementoSelezionatoCassaAnno0<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoCassaAnno1<s:property value="%{#suffix}" />"></td>
								<td class="tab_Right" id="modaleElementoSelezionatoCassaAnno2<s:property value="%{#suffix}" />"></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<a class="btn btn-primary" aria-hidden="true" id="pulsanteConfermaCapitolo<s:property value="%{#suffix}" />">conferma</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!--/modale  capitolo -->