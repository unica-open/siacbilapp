<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<h3>Elenco comunicazioni a PCC</h3>
			<div class="step-content">
				<div class="step-pane active">
					<fieldset class="form-horizontal">
						<h4 class="nostep-pane">Contabilizzazioni</h4>
						<table class="table table-hover tab_left" data-table="contabilizzazioni">
							<thead>
								<tr>
									<th>Stato del debito</th>
									<th>Causale</th>
									<th>Trasmesso</th>
									<th>Data invio</th>
									<th>Esito</th>
									<th>Data esito</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaContabilizzazioni" var="cds">
									<tr>
										<td><s:property value="#cds.statoDebito"/></td>
										<td><s:property value="#cds.causalePCC"/></td>
										<td><s:property value="#cds.trasmesso"/></td>
										<td><s:property value="#cds.dataInvio"/></td>
										<td>											
											<a href="#" data-trigger="hover" data-toggle="popover" data-placement="left" data-content="<s:property value='#cds.descrizioneEsito'/>">
												<s:property value='#cds.codiceEsito'/>
											</a>											 
										</td>
										<td><s:property value="#cds.dataEsito"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<div class="Border_line margin-medium"></div>
						<h4 class="nostep-pane">Comunicazioni data scadenza</h4>
						<table class="table table-hover tab_left" data-table="comunicazioniDataScadenza">
							<thead>
								<tr>
									<th>Data scadenza</th>
									<th>Trasmesso</th>
									<th>Data invio</th>
									<th>Esito</th>									
									<th>Data esito</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaComunicazioneDataScadenza" var="cds">
									<tr>
										<td><s:property value="#cds.dataScadenza"/></td>
										<td><s:property value="#cds.trasmesso"/></td>
										<td><s:property value="#cds.dataInvio"/></td>
										<td>											
											<a href="#" data-trigger="hover" data-placement="left" data-toggle="popover" data-content="<s:property value='#cds.descrizioneEsito'/>">
												<s:property value='#cds.codiceEsito'/>
											</a>											 
										</td>
										<td><s:property value="#cds.dataEsito"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
						<div class="Border_line margin-medium"></div>
						<h4 class="nostep-pane">Comunicazioni pagamento</h4>
						<table class="table table-hover tab_left" data-table="comunicazioniPagamento">
							<thead>
								<tr>
									<th>Data emissione ordinativo</th>
									<th>Ordinativo</th>
									<th>Data quietanza</th>
									<th>Quietanza</th>
									<th>Importo quietanza</th>											
									<th>Trasmesso</th>
									<th>Data invio</th>
									<th>Esito</th>									
									<th>Data esito</th>
								</tr>
							</thead>
							<tbody>
								<s:iterator value="listaComunicazionePagamento" var="cds">
									<tr>
										<td><s:property value="#cds.dataEmissioneOrdinativo"/></td>										
										<td><s:property value="#cds.numeroOrdinativo"/></td>										
										<td><s:property value="#cds.dataQuietanza"/></td>
										<td><s:property value="#cds.numeroQuietanza"/></td>
										<td><s:property value="#cds.importoQuietanza"/></td>
										<td><s:property value="#cds.trasmesso"/></td>
										<td><s:property value="#cds.dataInvio"/></td>
										<td>											
											<a href="#" data-trigger="hover" data-toggle="popover" data-placement="left" data-content="<s:property value='#cds.descrizioneEsito'/>">
												<s:property value='#cds.codiceEsito'/>
											</a>											 
										</td>
										<td><s:property value="#cds.dataEsito"/></td>
									</tr>
								</s:iterator>
							</tbody>
						</table>
					</fieldset>
				</div>
			</div>
		</div>
	</div>
</div>