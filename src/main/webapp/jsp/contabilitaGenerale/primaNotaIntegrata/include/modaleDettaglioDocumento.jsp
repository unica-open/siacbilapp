<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleDettaglioDocumento" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDettaglioDocumento">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="labelModaleDettaglioDocumento">Dati Movimento</h3>
	</div>
	<div class="modal-body">
		<div class="boxOrSpan2">
			<div class="boxOrInLeft">
				<p>Dati documento</p>
				<ul class="htmlelt">
					<li>
						<dfn>Data</dfn>
						<dl><s:property value="documento.dataEmissione" />&nbsp;</dl>
					</li>
					<li>
						<dfn>Soggetto</dfn>
						<dl><s:property value="documento.soggetto.codiceSoggetto" />&nbsp;</dl>
					</li>
					<li>
						<dfn>Beneficiario multiplo</dfn>
						<dl><s:if test="documento.flagBeneficiarioMultiplo">s&igrave;</s:if><s:else>no</s:else>&nbsp;</dl>
					</li>
					<li>
						<dfn>Descrizione</dfn>
						<dl><s:property value="documento.descrizione" />&nbsp;</dl>
					</li>
				</ul>
			</div>
			<div class="boxOrInRight">
				<p>Altri Dati</p>
				<ul class="htmlelt">
					<li>
						<dfn>Data Scadenza</dfn>
						<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
					</li>
					<li>
						<dfn>Codice Bollo</dfn>
						<dl>
							<s:if test="%{documento.codiceBollo != null}">
								<s:property value="%{documento.codiceBollo.codice + ' - ' + documento.codiceBollo.descrizione}"/>
							</s:if>&nbsp;
						</dl>
					</li>
					<li>
						<dfn>Note credito</dfn>
						<dl><s:property value="noteCredito" escapeHtml="false" /></dl>
					</li>
					<li>
						<dfn>Documenti collegati</dfn>
						<dl><s:property value="documentiCollegati" escapeHtml="false" /></dl>
					</li>
					<li>
						<dfn>Importi</dfn>
						<dl><s:property value="documento.importo"/>&nbsp;</dl>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
	</div>
</div>