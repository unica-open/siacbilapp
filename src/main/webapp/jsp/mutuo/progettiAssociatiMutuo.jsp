<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<s:include value="/jsp/include/head.jsp" />
</head>

<body>
	<s:include value="/jsp/include/header.jsp" />
	
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp"/>
               <s:form id="formProgettiAssociatiMutuo" cssClass="form-horizontal" >
                   <s:hidden name="mutuo.uid" />
                   <span class="hide" data-anno-bilancio='<s:property value="getAnnoBilancio()"/>'></span>
                 		
                     <h3>Progetti associati al mutuo</h3>

                  <div class="step-content">
                     <div class="step-pane active" id="step1">

                       <fieldset class="form-horizontal">
                           <h4>Dati mutuo</h4>
                           <div class="control-group">
                              <label class="control-label">Numero Mutuo</label>
                              <div class="controls">
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.numero" id="numeroMutuo" />
                                 
                                 <span class="al"> <label class="radio inline" for="tipoTasso">Tipo Tasso</label>
                                 </span> <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.tipoTasso" id="tipoTasso"/>

                                 <span class="al"> <label class="radio inline" for="sommaMutuataIniziale">Importo</label></span>
                                 <s:textfield disabled="true" id="sommaMutuataIniziale" name="mutuo.sommaMutuataIniziale" cssClass="span2 decimale soloNumeri forzaVirgolaDecimale"  />
                              </div>
                           </div>

                           <div class="control-group">
                              <label class="control-label">Istituto di Credito</label>
                              <div class="controls">
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span1" name="mutuo.soggetto.codiceSoggetto" id="codiceSoggetto" />
                                 <s:textfield type="text" disabled="true" class="lbTextSmall span6" name="mutuo.soggetto.denominazione" />
                              </div>
                           </div>

                           <s:if test="not mutuo.elencoProgettiAssociati.empty">
                              <s:include value="/jsp/mutuo/include/elencoProgettiAssociatiMutuo.jsp" />
                           </s:if><s:else>
                              Non sono presenti progetti associati.
                           </s:else>

                      <p/>  
                  <br/>
                         </fieldset>
                     </div>
                  </div>
                  <br/>
						<p>
							
                     		 <s:include value="/jsp/include/indietro.jsp" />
                     		 
                             <s:submit value="esporta progetti associati in excel" cssClass="btn disableOverlay" action="progettiAssociatiMutuo_scaricaElencoExcel" disabled="mutuo.elencoProgettiAssociati.empty"/>
                             <s:submit id="eliminaAssociazione" value="elimina associazione progetti selezionati" disabled="true" cssClass="btn" action="progettiAssociatiMutuo_eliminaAssociazione" />
							 <s:submit value="cerca progetti" cssClass="btn" action="ricercaProgettoMutuo" />
							 
						</p>
                  
                                    <s:include value="/jsp/mutuo/include/dettaglioMovimentoGestioneMutuo_modale.jsp" />
                  
					</s:form>
				</div>	
			</div>	
		</div>	 
	</div>	

	<s:include value="/jsp/include/footer.jsp" />

	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/mutuo/progettiAssociatiMutuo.js"></script>
</body>
</html>